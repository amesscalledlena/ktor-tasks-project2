package com.example.application.task.commands.handlers

import com.example.application.task.commands.models.DeleteTaskCommand
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.task.TaskId
import com.example.domain.valueobjects.task.UserId
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

        val result: Result<Boolean, TaskError> = transaction {
            // Rebuild the Task entity by replaying its history inside the transaction context
            val task = eventStoreRepository.getEventStream(idVO)

            when (val deletedTask = task.delete(userIdVO)) {
                is Result.Failure -> {
                    rollback()
                    return@transaction Result.failure(deletedTask.failure)
                }
                is Result.Success -> {
                    eventStoreRepository.append(task.getRecordedEvents())
                    repository.delete(idVO.value)
                    return@transaction Result.success(true)
                }
            }
        }

        return result
    }
}

