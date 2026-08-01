package com.example.infrastructure.repositories.task

import com.example.domain.entities.Task
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.task.TaskCategory
import com.example.domain.valueobjects.task.TaskId
import com.example.domain.valueobjects.task.TaskPriority
import com.example.domain.valueobjects.task.TaskStatus
import com.example.infrastructure.tables.TaskQm
import com.example.presentation.dtos.task.TaskDto
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert

class ExposedTaskRepository : TaskRepository {
    override fun save(task: Task) {
        TaskQm.upsert {
            it[TaskQm.id] = task.id.value
            it[TaskQm.title] = task.title.value
            it[TaskQm.description] = task.description.value
            it[TaskQm.updatedAt] = task.updatedAt
            it[TaskQm.createdAt] = task.createdAt
            it[status] = task.status.name
            it[priority] = task.priority.name
            it[category] = task.category.value
            it[dueDate] = task.dueDate
        }
    }

    override fun findById(id: TaskId): TaskDto? {
        return TaskQm.selectAll()
            .where { TaskQm.id eq id.value }
            .map { row ->
                TaskDto(
                    id = row[TaskQm.id],
                    title = row[TaskQm.title],
                    description = row[TaskQm.description],
                    createdAt = row[TaskQm.createdAt],
                    updatedAt = row[TaskQm.updatedAt],
                    dueDate = row[TaskQm.dueDate],
                    status = row[TaskQm.status],
                    priority = row[TaskQm.priority],
                    category = row[TaskQm.category]
                )
            }.singleOrNull()
    }

    override fun findAllPaginated(
        limit: Int,
        offset: Long,
        status: TaskStatus?,
        priority: TaskPriority?,
        category: TaskCategory?
    ): List<TaskDto> {
        val query =TaskQm.selectAll()
        if(status != null){
            query.andWhere { TaskQm.status eq status.name }
        }
        if(priority != null){
            query.andWhere { TaskQm.priority eq priority.name }
        }
        if(category != null){
            query.andWhere { TaskQm.category eq category.value }
        }

        return query.limit(limit)
            .offset(offset)
            .map { row ->
                TaskDto(
                    id = row[TaskQm.id],
                    title = row[TaskQm.title],
                    description = row[TaskQm.description],
                    createdAt = row[TaskQm.createdAt],
                    updatedAt = row[TaskQm.updatedAt],
                    dueDate = row[TaskQm.dueDate],
                    status = row[TaskQm.status],
                    priority = row[TaskQm.priority],
                    category = row[TaskQm.category]
                )
            }
    }

    override fun delete(id: String): Boolean {
        val deletedRowCount = TaskQm.deleteWhere { TaskQm.id eq id}
        return deletedRowCount > 0
    }

    override fun count(status: TaskStatus?, priority: TaskPriority?, category: TaskCategory?): Long {
        val query = TaskQm.selectAll()

        if (status != null) {
            query.andWhere { TaskQm.status eq status.name }
        }
        if (priority != null) {
            query.andWhere { TaskQm.priority eq priority.name }
        }
        if (category != null) {
            query.andWhere { TaskQm.category eq category.value }
        }

        return query.count()
    }
}