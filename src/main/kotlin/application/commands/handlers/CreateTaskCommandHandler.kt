package com.example.application.commands.handlers

import com.example.application.commands.models.CreateTaskCommand
import com.example.domain.entities.Task
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CreateTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
) {
    fun execute(command: CreateTaskCommand): Result<TaskId, TaskError> {
        val titleVO = when(val res = TaskTitle.create(command.title)){
            is Result.Success -> res.value
            is Result.Failure -> {return Result.Failure(res.failure)}
        }

        val descVO = when (val res = TaskDescription.create(command.description)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.Failure(res.failure)
        }

        val userIdVO = when (val res = UserId.create(command.userId)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.Failure(res.failure)
        }

        val categoryVO = when (val res = TaskCategory.create(command.category)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.Failure(res.failure)
        }

        val priorityVO = when (val res = TaskPriority.create(command.priority)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        val newTaskId = TaskId.generate()

        val taskResult = Task.makeNew(
            id = newTaskId,
            title = titleVO,
            description = descVO,
            userId = userIdVO,
            priority = priorityVO,
            category = categoryVO,
            dueDate = command.dueDate,
        )

        val task = when(taskResult){
            is Result.Success -> taskResult.value
            is Result.Failure -> return Result.Failure(taskResult.failure)
        }

        transaction {
            for(event in task.getRecordedEvents()){
                eventStoreRepository.append(event)
            }
            repository.save(task)
        }

        return Result.Success(task.id)
    }
}