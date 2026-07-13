package com.example.application.commands.handlers

import com.example.application.commands.models.DeleteTaskCommand
import com.example.domain.repository.TaskRepository
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  DeleteTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: DeleteTaskCommand): Boolean {
        return transaction {
            repository.delete(command.id)
        }
    }
}