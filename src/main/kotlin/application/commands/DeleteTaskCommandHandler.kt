package com.example.application.commands

import com.example.domain.TaskRepository

class  DeleteTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: DeleteTaskCommand): Boolean {
        return repository.delete(command.id)
    }
}