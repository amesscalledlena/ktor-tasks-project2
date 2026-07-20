package com.example.domain.events

import com.example.domain.interfaces.DomainEvent
import java.time.Instant
import java.util.UUID

data class TaskCreatedEvent(
    val taskId: Int,
    val taskTitle: String,
    override val eventId: UUID = UUID.randomUUID(),
    override val occurredOn: Instant = Instant.now(),
) : DomainEvent