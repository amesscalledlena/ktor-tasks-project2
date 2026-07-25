package com.example.application.commands.handlers

import com.example.application.commands.models.CompleteTaskCommand
import com.example.domain.entities.Task
import com.example.domain.events.core.TaskCompletedEvent
import com.example.domain.events.core.TaskEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.UserId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant

class CompleteTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
    ) {
    fun execute(command: CompleteTaskCommand): Result<Boolean, TaskError> {

        val idVO = when (val res = TaskId.create(command.id)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        val userIdVO = when (val res = UserId.create(command.userId)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        val eventStream = eventStoreRepository.getEventStream(idVO).map { it as TaskEvent } // Fetch ALL past events with this aggregate/task id
        if (eventStream.isEmpty()) {
            return Result.failure(TaskError.InvalidTitle("Task with ID ${command.id} not found"))
        }

        // Rebuild the Task entity by replaying its history
        val task = Task.fromDatabase(eventStream)

        val completedTask = task.complete(userIdVO)
        when (completedTask) {
            is Result.Failure -> return Result.failure(completedTask.failure)
            is Result.Success -> {}
        }

        transaction {
            eventStoreRepository.append(task.getRecordedEvents())
            repository.update(task)
        }

        return Result.success(true)
    }
}