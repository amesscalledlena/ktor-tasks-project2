package com.example.domain.events.core

import com.example.domain.events.interfaces.EventId
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
sealed interface DomainEvent {
    val eventId: UUID // Unique identifier for the event to prevent idempotency
    val taskId: EventId
    val occurredOn: Instant // Timestamp of the exact millisecond the event took place
}