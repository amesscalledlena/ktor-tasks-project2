package com.example.application.commands.handlers

import com.example.application.commands.models.CompleteTaskCommand
import com.example.domain.events.core.TaskCompletedEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant

class CompleteTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
    ) {
    fun execute(command: CompleteTaskCommand): Result<Boolean, TaskError> {
        val existingTask = transaction{
            repository.findById(command.id)
        }

        if (existingTask == null) {
            return Result.Success(false ) // Exit immediately
        }

        val idVO = TaskId.create(command.id)
            .onFailure {  }.successOrException

        val result = transaction {
            val updateTime = Instant.now()
            existingTask.complete(updateTime)
            repository.update(existingTask)
            
            val event = TaskCompletedEvent(
                taskId = idVO.value,
            )

            eventStoreRepository.append(event)
        }

        return Result.Success(true)
    }
}