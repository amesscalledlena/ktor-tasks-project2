package com.example.application.commands.handlers

import com.example.application.commands.models.DeleteTaskCommand
import com.example.domain.events.TaskCompletedEvent
import com.example.domain.events.TaskDeletedEvent
import com.example.domain.interfaces.EventBus
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.TaskId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  DeleteTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventBus: EventBus,
) {
    suspend fun execute(command: DeleteTaskCommand): Result<Boolean, TaskError> {
        val idVO = TaskId.create(command.id)

        if (idVO.isFailure){
            return Result.failure(idVO.failureOrException)
        }
        val validId = idVO.successOrException.value

        val result= transaction {
            val wasDeleted = repository.delete(validId)
            Result.success(wasDeleted)
        }

        if (result is Result.Success && result.value) {
            eventBus.publish(
                event = TaskDeletedEvent(
                    taskId = validId,
                )
            )
        }

        return result
    }
}

