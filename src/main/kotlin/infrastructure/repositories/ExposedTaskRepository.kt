package com.example.infrastructure.repositories

import com.example.domain.entities.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import com.example.infrastructure.tables.TaskTbl
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant

class ExposedTaskRepository : TaskRepository {
    override fun save(task: Task): Int {
        return TaskTbl.insert {
                it[TaskTbl.title] = task.title.value
                it[TaskTbl.description] = task.description.value
                it[TaskTbl.updatedAt] = Instant.now()
            } get TaskTbl.id
    }
    override fun findById(id: Int): Task? {
        return TaskTbl.selectAll().where { TaskTbl.id eq id }.map { row ->
            Task(
                id = TaskId(row[TaskTbl.id]),
                title = TaskTitle(row[TaskTbl.title]),
                description = TaskDescription(row[TaskTbl.description]),
                updatedAt = row[TaskTbl.updatedAt],
                isCompleted = row[TaskTbl.isCompleted],
            )
            }.singleOrNull()
    }
    override fun findAllPaginated(limit: Int, offset: Long): List<Task> {
        return TaskTbl.selectAll()
                .limit(limit)
                .offset(offset)
                .map { row ->
                    Task(
                        id = TaskId(row[TaskTbl.id]),
                        title = TaskTitle(row[TaskTbl.title]),
                        description = TaskDescription(row[TaskTbl.description]),
                        updatedAt = row[TaskTbl.updatedAt],
                        isCompleted = row[TaskTbl.isCompleted],
                    )
                }
    }
    override fun count(): Long {
        return TaskTbl.selectAll().count()
    }

    override fun update(task: Task): Boolean {
        val updatedRowCount = TaskTbl.update({ TaskTbl.id eq task.id.value }) {
                it[TaskTbl.title] = title
                it[TaskTbl.description] = description
                it[TaskTbl.updatedAt] = Instant.now()
                it[TaskTbl.isCompleted] =isCompleted
            }
        return updatedRowCount > 0
    }

    override fun delete(id: Int): Boolean {
        val deletedRowCount = TaskTbl.deleteWhere { TaskTbl.id eq id }
        return deletedRowCount > 0
        }
}