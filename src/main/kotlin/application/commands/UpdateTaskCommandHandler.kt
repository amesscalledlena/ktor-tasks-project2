package com.example.application.commands

import com.example.domain.TaskRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  UpdateTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: UpdateTaskCommand): Boolean {
        return transaction {
            val titleVO = TaskTitle(command.title)
            val descriptionVO = TaskDescription(command.description)

            val existingTask = repository.findById(command.id) ?: return@transaction false
                                                            // ?: throw IllegalArgumentException("Task with ID ${command.id} not found")
            existingTask.update(titleVO, descriptionVO)
            repository.update(existingTask)
        }
    }
}