package com.example.application.commands

import com.example.domain.TaskRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskTitle

class  UpdateTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: UpdateTaskCommand): Boolean {
        val titleVO = TaskTitle(command.title)
        val descriptionVO = TaskDescription(command.description)

        val existingTask = repository.findById(command.id) ?: return false

        existingTask.update(titleVO, descriptionVO)
        return repository.update(existingTask)
    }
}