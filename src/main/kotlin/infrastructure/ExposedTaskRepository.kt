package com.example.infrastructure

import com.example.domain.Task
import com.example.domain.TaskDescription
import com.example.domain.TaskId
import com.example.domain.TaskRepository
import com.example.domain.TaskTitle
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant

class ExposedTaskRepository : TaskRepository {
    override fun save(task: Task): Int {
        return transaction {
            TaskTbl.insert {
                it[TaskTbl.title] = title
                it[TaskTbl.description] = description
                it[TaskTbl.updatedAt] = Instant.now()
            } get TaskTbl.id
        }
    }
    override fun findById(id: Int): Task? {
        return transaction {
            TaskTbl.selectAll().where { TaskTbl.id eq id }.map { row ->
                Task(
                    id = TaskId( row[TaskTbl.id]),
                    title = TaskTitle(row[TaskTbl.title]),
                    description = TaskDescription(row[TaskTbl.description]),
                    updatedAt = row[TaskTbl.updatedAt],
                    isCompleted = row[TaskTbl.isCompleted],
                )
            }.singleOrNull()
        }
    }
    override fun findAllPaginated(limit: Int, offset: Long): List<Task> {
        return transaction {
            TaskTbl.selectAll()
                .limit(limit)
                .offset(offset)
                .map { row ->
                    Task(
                        id = TaskId( row[TaskTbl.id]),
                        title = TaskTitle( row[TaskTbl.title]),
                        description = TaskDescription( row[TaskTbl.description]),
                        updatedAt = row[TaskTbl.updatedAt],
                        isCompleted = row[TaskTbl.isCompleted],
                    )
                }
        }
    }
    override fun count(): Long {
        return transaction {
            TaskTbl.selectAll().count()
        }
    }

    override fun update(task: Task): Boolean {
        return transaction {
            val updatedRowCount = TaskTbl.update({ TaskTbl.id eq task.id.value }) {
                it[TaskTbl.title] = title
                it[TaskTbl.description] = description
                it[TaskTbl.updatedAt] = Instant.now()
                it[TaskTbl.isCompleted] =isCompleted
            }
            updatedRowCount > 0 //The return
        }
    }

    override fun delete(id: Int): Boolean {
        return transaction {
            val deletedRowCount = TaskTbl.deleteWhere { TaskTbl.id eq id }
            deletedRowCount > 0 //The return
        }
    }
}