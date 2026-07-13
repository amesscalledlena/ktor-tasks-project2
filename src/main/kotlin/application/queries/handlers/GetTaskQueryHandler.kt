package com.example.application.queries.handlers

import com.example.application.queries.models.GetTaskQuery
import com.example.domain.entities.Task
import com.example.domain.interfaces.TaskRepository
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class GetTaskQueryHandler(private val repository: TaskRepository) {
    fun execute(query: GetTaskQuery): Result<Task?> {
        return transaction {
            runCatching {
                repository.findById(query.id)
            }
        }
    }
}