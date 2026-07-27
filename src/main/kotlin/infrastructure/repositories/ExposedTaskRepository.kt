package com.example.infrastructure.repositories

import com.example.domain.entities.Task
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.TaskId
import com.example.infrastructure.tables.TaskQm
import com.example.presentation.dtos.TaskDto
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant

class ExposedTaskRepository : TaskRepository {
    override fun save(task: Task) {
        val existingRowCount = TaskQm.selectAll().where{TaskQm.id eq task.id.value}.count()
        if(existingRowCount > 0){
            TaskQm.update({ TaskQm.id eq task.id.value }) {
                it[TaskQm.title] = task.title.value
                it[TaskQm.description] = task.description.value
                it[TaskQm.updatedAt] = task.updatedAt
                it[status] = task.status.name
                it[priority] = task.priority.name
                it[category] = task.category.value
                it[dueDate] = task.dueDate
            }
        }else {
            TaskQm.insert {
                it[TaskQm.id] = task.id.value
                it[TaskQm.title] = task.title.value
                it[TaskQm.description] = task.description.value
                it[TaskQm.updatedAt] = Instant.now()
                it[TaskQm.createdAt] = task.createdAt
                it[status] = task.status.name
                it[priority] = task.priority.name
                it[category] = task.category.value
                it[dueDate] = task.dueDate
            }
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

    override fun findAllPaginated(limit: Int, offset: Long): List<TaskDto> {
        return TaskQm.selectAll()
            .limit(limit)
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

    override fun count(): Long {
        return TaskQm.selectAll().count()
    }
}