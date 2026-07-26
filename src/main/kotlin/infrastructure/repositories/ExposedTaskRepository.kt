package com.example.infrastructure.repositories

import com.example.domain.entities.Task
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.TaskId
import com.example.infrastructure.tables.TaskTbl
import com.example.presentation.dtos.TaskDto
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant

class ExposedTaskRepository : TaskRepository {
    override fun save(task: Task) {
        TaskTbl.insert {
            it[TaskTbl.id] = task.id.value
            it[TaskTbl.title] = task.title.value
            it[TaskTbl.description] = task.description.value
            it[TaskTbl.updatedAt] = Instant.now()
            it[TaskTbl.createdAt] = task.createdAt
            it[status] = task.status.name
            it[priority] = task.priority.name
            it[category] = task.category.value
            it[dueDate] = task.dueDate
        }
    }

    override fun findById(id: TaskId): TaskDto? {
        return TaskTbl.selectAll()
            .where { TaskTbl.id eq id.value }
            .map { row ->
                TaskDto(
                    id = row[TaskTbl.id],
                    title = row[TaskTbl.title],
                    description = row[TaskTbl.description],
                    createdAt = row[TaskTbl.createdAt],
                    updatedAt = row[TaskTbl.updatedAt],
                    dueDate = row[TaskTbl.dueDate],
                    status = row[TaskTbl.status],
                    priority = row[TaskTbl.priority],
                    category = row[TaskTbl.category]
                )
            }.singleOrNull()
    }

    override fun findAllPaginated(limit: Int, offset: Long): List<TaskDto> {
        return TaskTbl.selectAll()
            .limit(limit)
            .offset(offset)
            .map { row ->
                TaskDto(
                    id = row[TaskTbl.id],
                    title = row[TaskTbl.title],
                    description = row[TaskTbl.description],
                    createdAt = row[TaskTbl.createdAt],
                    updatedAt = row[TaskTbl.updatedAt],
                    dueDate = row[TaskTbl.dueDate],
                    status = row[TaskTbl.status],
                    priority = row[TaskTbl.priority],
                    category = row[TaskTbl.category]
                )
            }
    }

    override fun update(task: Task): Boolean {
        val updatedRowCount = TaskTbl.update({ TaskTbl.id eq task.id.value }) {
            it[TaskTbl.title] = task.title.value
            it[TaskTbl.description] = task.description.value
            it[TaskTbl.updatedAt] = task.updatedAt
            it[status] = task.status.name
            it[priority] = task.priority.name
            it[category] = task.category.value
            it[dueDate] = task.dueDate
        }
        return updatedRowCount > 0
    }

    override fun delete(id: String): Boolean {
        val deletedRowCount = TaskTbl.deleteWhere { TaskTbl.id eq id}
        return deletedRowCount > 0
    }

    override fun count(): Long {
        return TaskTbl.selectAll().count()
    }
}