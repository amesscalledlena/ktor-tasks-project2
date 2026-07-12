package com.example.infrastructure

import com.example.domain.Task
import com.example.domain.TaskRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Instant

class ExposedTaskRepository : TaskRepository {
    override fun insert(title: String, description: String, updatedAt: String): Int {
        return transaction {
            TaskTbl.insert {
                it[TaskTbl.title] = title
                it[TaskTbl.description] = description
                it[TaskTbl.updatedAt] = Instant.now().toString()
            } get TaskTbl.id
        }
    }
    override fun findById(id: Int): Task? {
        return transaction {
            TaskTbl.selectAll().where { TaskTbl.id eq id }.map { row ->
                Task(
                    id = row[TaskTbl.id],
                    title = row[TaskTbl.title],
                    description = row[TaskTbl.description],
                    updatedAt = row[TaskTbl.updatedAt].toString(),
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
                        id = row[TaskTbl.id],
                        title = row[TaskTbl.title],
                        description = row[TaskTbl.description],
                        updatedAt = row[TaskTbl.updatedAt].toString(),
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

    override fun update(id: Int, title: String, description: String, updatedAt: String, isCompleted: Boolean): Boolean {
        return transaction {
            val updatedRowCount = TaskTbl.update({ TaskTbl.id eq id }) {
                it[TaskTbl.title] = title
                it[TaskTbl.description] = description
                it[TaskTbl.updatedAt] = Instant.now().toString()
                it[TaskTbl.isCompleted] = isCompleted
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