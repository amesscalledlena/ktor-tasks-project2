package com.example.application.commands.handlers

import com.example.application.commands.models.CreateTaskCommand
import com.example.domain.entities.Task
import com.example.domain.events.core.TaskCreatedEvent
import com.example.domain.events.interfaces.EventId
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.Result
import com.example.domain.railway.Result.Companion.zip
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import com.example.domain.valueobjects.UserId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant
import java.util.UUID

class CreateTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventStoreRepository: EventStoreRepository,
) {
    fun execute(command: CreateTaskCommand): Result<TaskId, TaskError> {
        val titleVO = TaskTitle.create(command.title)
        val descVO = TaskDescription.create(command.description)

//        val result = zip(
//            a = titleVO,
//            b = descVO,
//            failure = TaskError.InvalidTitle("Multiple validation errors occurred"),
//            mapSuccess = { validTitle, validDesc ->
//                Task.makeNew(event)
//            }
//        )

        return when (result) {
            is Result.Failure -> {
                Result.failure(result.failure)
            }

            is Result.Success -> {
                val taskResult = result.value
                when (taskResult) {
                    is Result.Failure -> {
                        Result.failure(taskResult.failure)
                    }

                    is Result.Success -> {
                        val task = taskResult.value
                        transaction {
                            for (event in task.getRecordedEvents()) {
                                eventStoreRepository.append(event)
                            }

                            repository.save(task)
                        }

                        // Added explicit `return` keyword here to fix the missing return statement error
                        Result.success(task.id)
                    }
                }
            }
        }
    }
}