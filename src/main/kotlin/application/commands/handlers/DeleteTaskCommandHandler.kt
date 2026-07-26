package com.example.application.commands.handlers

import com.example.application.commands.models.DeleteTaskCommand
import com.example.domain.entities.Task
import com.example.domain.events.core.TaskEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.UserId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  DeleteTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
) {
    fun execute(command: DeleteTaskCommand): Result<Boolean, TaskError> {

        val idVO = when (val res = TaskId.create(command.id)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        val userIdVO = when (val res = UserId.create(command.userId)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        // Rebuild the Task entity by replaying its history
        val task = eventStoreRepository.getEventStream(idVO)
            ?: return Result.failure(TaskError.InvalidTitle("Task with ID ${command.id} not found"))

        val deletedTask = task.delete(userIdVO)
        when (deletedTask) {
            is Result.Failure -> return Result.failure(deletedTask.failure)
            is Result.Success -> {}
        }

        transaction {
            eventStoreRepository.append(task.getRecordedEvents())
            repository.delete(idVO.value)
        }

        return Result.success(true)
    }
}

