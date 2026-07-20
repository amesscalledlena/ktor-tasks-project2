package com.example.application.commands.handlers

import com.example.application.commands.models.UpdateTaskCommand
import com.example.domain.events.TaskCreatedEvent
import com.example.domain.events.TaskUpdatedEvent
import com.example.domain.interfaces.EventBus
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.railway.Result.Companion.zip
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class  UpdateTaskCommandHandler(
    private val repository: TaskRepository,
    private val eventBus: EventBus,
    ) {
    suspend fun execute(command: UpdateTaskCommand): Result<Boolean, TaskError> {
        val idVO = TaskId.create(command.id)
        val titleVO = TaskTitle.create(command.title)
        val descriptionVO = TaskDescription.create(command.description)

        val result = zip(
            a = idVO,
            b = titleVO,
            c = descriptionVO,
            failure = TaskError.InvalidTitle("Multiple validation errors occurred"),
            mapSuccess = {validId, validTitle, validDesc ->
                transaction {
                    val existingTask = repository.findById(validId.value)

                    if (existingTask == null){
                        false
                    }else{
                        existingTask.update(title = validTitle, description = validDesc)
                        repository.update(existingTask)
                    }
                }
            }
        )

        if(result is Result.Success && result.value){
            eventBus.publish(
                event = TaskUpdatedEvent(
                    taskId = command.id,
                    taskTitle = command.title,
                    taskDescription = command.description,
                )
            )
        }

        return result
    }
}
