package com.example.infrastructure.repositories

import com.example.domain.entities.Task
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.TaskId
import com.example.infrastructure.tables.TaskTbl
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant
import java.util.UUID

class ExposedTaskRepository : TaskRepository {
    override fun save(task: Task) {
        TaskTbl.insert {
            it[TaskTbl.id] = task.id.value.toString()
            it[TaskTbl.title] = task.title.value
            it[TaskTbl.description] = task.description.value
            it[TaskTbl.updatedAt] = Instant.now()
            it[TaskTbl.isCompleted] = task.isCompleted
        }
    }

    override fun findById(id: TaskId): Task? {

        return TaskTbl.selectAll()
            .where { TaskTbl.id eq id.value }
            .map { row ->
//                Task.fromDatabase(
//                    rawId = row[TaskTbl.id],
//                    rawTitle = row[TaskTbl.title],
//                    rawDescription = row[TaskTbl.description],
//                    rawUpdatedAt = row[TaskTbl.updatedAt],
//                    rawIsCompleted = row[TaskTbl.isCompleted],
//                TODO : add TaskDto
            )
        }.singleOrNull()
    }

    override fun findAllPaginated(limit: Int, offset: Long): List<Task> {
        return TaskTbl.selectAll()
            .limit(limit)
            .offset(offset)
            .map { row ->
                Task.fromDatabase(
                    rawId = row[TaskTbl.id],
                    rawTitle = row[TaskTbl.title],
                    rawDescription = row[TaskTbl.description],
                    rawUpdatedAt = row[TaskTbl.updatedAt],
                    rawIsCompleted = row[TaskTbl.isCompleted],
                )
            }
    }

    override fun count(): Long {
        return TaskTbl.selectAll().count()
    }

    override fun update(task: Task): Boolean {
        val updatedRowCount = TaskTbl.update({ TaskTbl.id eq task.id.value.toString() }) {
            it[TaskTbl.title] = task.title.value
            it[TaskTbl.description] = task.description.value
            it[TaskTbl.updatedAt] = task.updatedAt
            it[TaskTbl.isCompleted] = task.isCompleted
        }
        return updatedRowCount > 0
    }

    override fun delete(id: TaskId): Boolean {
        val deletedRowCount = TaskTbl.deleteWhere { TaskTbl.id eq id.value.toString()}
        return deletedRowCount > 0
    }
}