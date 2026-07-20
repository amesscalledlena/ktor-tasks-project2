package com.example.application.commands.handlers

import com.example.application.commands.models.CompleteTaskCommand
import com.example.domain.events.TaskCompletedEvent
import com.example.domain.interfaces.EventBus
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.TaskId
import com.example.domain.railway.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant

class CompleteTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventBus: EventBus
    ) {
    suspend fun execute(command: CompleteTaskCommand): Result<Boolean, TaskError> {
        val idVO = TaskId.create(command.id)

        if (idVO.isFailure){
            return Result.failure(idVO.failureOrException)
        }

        val validId = idVO.successOrException.value

        val result = transaction {
            val existingTask = repository.findById(validId) ?: return@transaction Result.failure(TaskError.NotFound(command.id))
            val updateTime = Instant.now()
            existingTask.complete(updateTime)
            val isUpdated = repository.update(existingTask)
            Result.success(isUpdated)
        }

        if (result is Result.Success && result.value) {
            eventBus.publish(
                event = TaskCompletedEvent(
                    taskId = validId
                )
            )
        }
        return result
    }
}