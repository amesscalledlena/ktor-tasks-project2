package com.example.application.commands.handlers

import com.example.application.commands.models.DeleteTaskCommand
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.TaskId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  DeleteTaskCommandHandler(private val repository: TaskRepository) {
    fun execute(command: DeleteTaskCommand): Result<Boolean, TaskError> {
        val idVO = TaskId.create(command.id)

        if (idVO.isFailure){
            return Result.failure(idVO.failureOrException)
        }

        return transaction {
            val validId = idVO.successOrException.value
            val wasDeleted = repository.delete(validId)
            Result.success(wasDeleted)
        }
    }
}

