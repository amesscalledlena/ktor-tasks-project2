package com.example.domain.events

import com.example.domain.events.serializers.*
import com.example.domain.interfaces.DomainEvent
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class TaskCreatedEvent(
    val taskId: Int,
    val taskTitle: String,
    @Serializable(with = UUIDSerializer::class)
    override val eventId: UUID = UUID.randomUUID(),
    @Serializable(with = InstantSerializer::class)
    override val occurredOn: Instant = Instant.now(),
) : DomainEvent