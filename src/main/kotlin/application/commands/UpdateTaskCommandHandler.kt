package com.example.application.commands

import com.example.domain.TaskRepository
import java.time.Instant

class  UpdateTaskCommandHandler(private val repository: TaskRepository) {
    fun handle(command: UpdateTaskCommand): Boolean {
        val updatedAt = Instant.now().toString()
        return repository.update(command.id, command.title, command.description, updatedAt, command.isCompleted)
    }
}