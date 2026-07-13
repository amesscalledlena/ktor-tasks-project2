package com.example.application.commands.handlers

import com.example.application.commands.models.CompleteTaskCommand
import com.example.domain.repository.TaskRepository
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant

class CompleteTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: CompleteTaskCommand): Boolean {
        return transaction {
            val existingTask = repository.findById(command.id) ?: return@transaction false
            // ?: throw IllegalArgumentException("Task with ID ${command.id} not found")
            val updateTime = Instant.now()
            existingTask.complete(updateTime)
            repository.complete(existingTask)
        }
    }
}