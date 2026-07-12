package com.example.application.queries

import com.example.domain.Task
import com.example.domain.TaskRepository

class GetTaskQueryHandler(private val repository: TaskRepository) {
    fun handle(query: GetTaskQuery): Task? {
        return repository.findById(query.id)
    }
}