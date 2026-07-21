package com.example.domain.entities

import com.example.domain.events.TaskCreatedEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant

class Task(
    var id: TaskId,
    var title: TaskTitle,
    var description: TaskDescription,
    var updatedAt: Instant?,
    var isCompleted: Boolean,
    ) {
    companion object {
        fun create(
            id:TaskId,
            title: TaskTitle,
            description: TaskDescription,
            eventStoreRepository: EventStoreRepository,
        ): Task {
            val event =
                TaskCreatedEvent(
                taskId = id,
                taskTitle = title,
            )


            return Task(
                id = id,
                title = title,
                description = TaskDescription.fromDatabase(description.toString()),
                updatedAt = Instant.now(),
                isCompleted = false,
            )
        }
    }

    fun update(
        title: TaskTitle,
        description: TaskDescription,
    ){
        this.title = title
        this.description = description
    }

    fun complete(updatedAt: Instant) {
        this.isCompleted = true
        this.updatedAt = updatedAt
    }
}