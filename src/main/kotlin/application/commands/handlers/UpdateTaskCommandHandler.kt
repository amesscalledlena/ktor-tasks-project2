package com.example.application.commands.handlers

import com.example.application.commands.models.UpdateTaskCommand
import com.example.domain.events.TaskUpdatedEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.railway.Result.Companion.zip
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  UpdateTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
    ) {
    fun execute(command: UpdateTaskCommand): Result<Boolean, TaskError> {
        val idVO = TaskId.create(command.id)
        val titleVO = TaskTitle.create(command.title)
        val descriptionVO = TaskDescription.create(command.description)

        val existingTask = transaction {
            repository.findById(command.id)
        }

        if (existingTask == null) {
            return Result.Success(false ) // Exit immediately
        }

        val result = zip(
            a = idVO,
            b = titleVO,
            c = descriptionVO,
            failure = TaskError.InvalidTitle("Multiple validation errors occurred"),
            mapSuccess = { validId, validTitle, validDesc ->
                existingTask.update(
                    title = validTitle,
                    description = validDesc,
                )
                existingTask
                }
        ).onFailure{}.successOrException

        transaction {
            repository.update(result)

            val event = TaskUpdatedEvent(
                taskId = result.id.value,
                taskTitle = result.title.value,
                taskDescription = result.description.value,
            )

            eventStoreRepository.append(event)
        }


        return Result.Success(true)
    }
}