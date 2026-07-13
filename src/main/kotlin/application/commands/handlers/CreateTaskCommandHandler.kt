package com.example.application.commands.handlers

import com.example.application.commands.models.CreateTaskCommand
import com.example.domain.entities.Task
import com.example.domain.repository.TaskRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CreateTaskCommandHandler(
    private val repository: TaskRepository
) {
    fun execute(command: CreateTaskCommand): Int {
        return transaction {
            val titleVO = TaskTitle(command.title)
            val descriptionVO = TaskDescription(command.description)

            val newTask = Task.create(title = titleVO, description = descriptionVO)
            repository.save(newTask)
        }
    }
}