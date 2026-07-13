package com.example.application.commands.handlers

import com.example.application.commands.models.UpdateTaskCommand
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  UpdateTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: UpdateTaskCommand): Result<Boolean> {
        return transaction {
            runCatching {
                val titleVO = TaskTitle(command.title)
                val descriptionVO = TaskDescription(command.description)

                val existingTask = repository.findById(command.id) ?: throw IllegalArgumentException("Task with ID ${command.id} not found")
                existingTask.update(titleVO, descriptionVO)
                repository.update(existingTask)
            }
        }
    }
}