package com.example.application.commands.handlers

import com.example.application.commands.models.DeleteTaskCommand
import com.example.domain.interfaces.TaskRepository
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  DeleteTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: DeleteTaskCommand): Result<Boolean> {
        return transaction {
            val result = runCatching {
                repository.delete(command.id)
            }
            result.onFailure {
                rollback()
            }
        }
    }
}