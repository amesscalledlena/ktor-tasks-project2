package com.example.application.commands.handlers

import com.example.application.commands.models.DeleteTaskCommand
import com.example.domain.events.core.TaskDeletedEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  DeleteTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
) {
    fun execute(command: DeleteTaskCommand): Result<Boolean, TaskError> {
        val existingTask = transaction{
            repository.findById(command.id)
        }

        if (existingTask == null) {
            return Result.Success(false)
        }

        val idVO = TaskId.create(command.id)
            .onFailure {  }.successOrException


        transaction {
            repository.delete(idVO.value)

            val event = TaskDeletedEvent(
                taskId = idVO.value,
            )

            eventStoreRepository.append(event)
        }

        return Result.Success(true)
    }
}

