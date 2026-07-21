package com.example.application.commands.handlers

import com.example.application.commands.models.CreateTaskCommand
import com.example.domain.entities.Task
import com.example.domain.events.TaskCreatedEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.Result.Companion.zip
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CreateTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
) {
    fun execute(command: CreateTaskCommand): Result<Int, TaskError> {
        val titleVO = TaskTitle.create(command.title)
        val descVO = TaskDescription.create(command.description)

        val result = zip(
            a = titleVO,
            b = descVO,
            failure = TaskError.InvalidTitle("Multiple validation errors occurred"),
            mapSuccess = { validTitle, validDesc ->
                Task.create(
                    title = validTitle,
                    description = validDesc,
                    eventStoreRepository = eventStoreRepository,
                )
            }
        ).onFailure {  }
            .successOrException

        val newTaskId=transaction {
             val id = repository.save(result)

            id
            }

        return Result.Success(newTaskId)
    }
}
