package com.example.application.commands

import com.example.domain.Task
import com.example.domain.TaskRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskTitle

class CreateTaskCommandHandler(
    private val repository: TaskRepository
) {
    fun execute(command: CreateTaskCommand): Int {
        val titleVO = TaskTitle(command.title)
        val descriptionVO = TaskDescription(command.description)

        val newTask = Task.create(titleVO, descriptionVO)
        return repository.save(newTask)
    }
}