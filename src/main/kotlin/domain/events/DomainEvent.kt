package com.example.domain.events

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
sealed interface DomainEvent {
    val eventId: UUID // Unique identifier for the event to prevent idempotency
    val taskId: EventId // TODO ایجاد ش کن و بعد تسک ای دی از اون ارث می برد
    val occurredOn: Instant // Timestamp of the exact millisecond the event took place
}