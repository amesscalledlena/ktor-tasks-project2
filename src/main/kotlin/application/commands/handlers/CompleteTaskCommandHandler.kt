package com.example.application.commands.handlers

import com.example.application.commands.models.CompleteTaskCommand
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.TaskId
import com.example.domain.railway.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant

class CompleteTaskCommandHandler(private val repository: TaskRepository) {

    fun execute(command: CompleteTaskCommand): Result<Boolean, TaskError> {
        val idVO = TaskId.create(command.id)

        if (idVO.isFailure){
            return Result.failure(idVO.failureOrException)
        }

        val taskId = idVO.successOrException.value

        return transaction {
            val existingTask = repository.findById(taskId) ?: return@transaction Result.failure(TaskError.NotFound(command.id))
            val updateTime = Instant.now()
            existingTask.complete(updateTime)
            val isUpdated = repository.update(existingTask)
            Result.success(isUpdated)
        }
    }
}