package com.example.application.commands.handlers

import com.example.application.commands.models.CreateTaskCommand
import com.example.domain.entities.Task
import com.example.domain.events.TaskCreatedEvent
import com.example.domain.interfaces.EventBus
import com.example.domain.interfaces.TaskRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskTitle
import com.example.domain.railway.*
import com.example.domain.railway.Result.Companion.zip
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CreateTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventBus: EventBus
) {
    suspend fun execute(command: CreateTaskCommand): Result<Int, TaskError> {
        val titleVO = TaskTitle.create(command.title)
        val descVO = TaskDescription.create(command.description)

        val result = zip(
            a = titleVO,
            b = descVO,
            failure = TaskError.InvalidTitle("Multiple validation errors occurred"),
            mapSuccess = { validTitle, validDesc ->
                val id = transaction { // Use transaction only when u r writing to the db
                    val task = Task.create(
                        title = validTitle,
                        description = validDesc,
                    )
                    repository.save(task)
                }

                id
            }
        )

        if(result is Result.Success){
            eventBus.publish(
                event = TaskCreatedEvent(
                    taskId = result.value,
                    taskTitle = command.title,
                )
            )
        }

        return result
    }
}
