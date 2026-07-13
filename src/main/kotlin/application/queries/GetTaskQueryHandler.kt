package com.example.application.queries

import com.example.domain.Task
import com.example.domain.TaskRepository
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class GetTaskQueryHandler(private val repository: TaskRepository) {
    fun execute(query: GetTaskQuery): Task? {
        return transaction{
            repository.findById(query.id)
        }
    }
}