package com.example.application.commands.handlers

import com.example.application.commands.models.UpdateTaskCommand
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import com.example.domain.valueobjects.UserId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  UpdateTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
    ) {
    fun execute(command: UpdateTaskCommand): Result<Boolean, TaskError> {
        val idVO = when (val res = TaskId.create(command.id)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        val titleVO = when (val res = TaskTitle.create(command.title)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        val descriptionVO = when (val res = TaskDescription.create(command.description)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        val userIdVO = when (val res = UserId.create(command.userId)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        //TODO:- private constructor of Task

        // Rebuild the Task entity by replaying its history
        val task = eventStoreRepository.getEventStream(idVO)

        val updatedTask = task.update(
            userId = userIdVO,
            title = titleVO,
            description = descriptionVO
        )
        when (updatedTask) {
            is Result.Failure -> return Result.failure(updatedTask.failure)
            is Result.Success -> {}
        }

        transaction {
            eventStoreRepository.append(task.getRecordedEvents())
            repository.update(task)
        }

        return Result.success(true)

    }
}