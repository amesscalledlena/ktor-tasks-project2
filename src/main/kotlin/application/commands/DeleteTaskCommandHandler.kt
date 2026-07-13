package com.example.application.commands

import com.example.domain.TaskRepository
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  DeleteTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: DeleteTaskCommand): Boolean {
        return transaction{
            repository.delete(command.id)
        }
    }
}