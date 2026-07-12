package com.example.application.commands

import com.example.domain.TaskRepository
import java.time.Instant

class CreateTaskCommandHandler(private val repository: TaskRepository) {
    fun handle(command: CreateTaskCommand): Int {
        val updatedAt = Instant.now().toString()
        return repository.insert(command.title, command.description, updatedAt)
    }
}