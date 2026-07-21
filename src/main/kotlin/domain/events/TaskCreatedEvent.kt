package com.example.domain.events

import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import java.time.Instant
import java.util.*

data class TaskCreatedEvent(
    override val taskId: TaskId,
    val taskTitle: TaskTitle,
    override val eventId: UUID = UUID.randomUUID(),
    override val occurredOn: Instant = Instant.now(),
) : DomainEvent