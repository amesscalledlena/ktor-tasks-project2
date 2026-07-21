package com.example.domain.interfaces

import java.time.Instant
import java.util.UUID

interface DomainEvent {
    val eventId: UUID // Unique identifier for the event to prevent idempotency
    val taskId: Int
    val occurredOn: Instant // Timestamp of the exact millisecond the event took place
}

/*
It doesn't need any methods at all.
In Event-Driven Design, a DomainEvent is primarily a marker interface.
Its main job is to group your event classes under a single type so your EventBus or Publisher knows how to handle them.
Events are pure data structures representing a fact about something that happened in the past—they don't do anything, they just are.
*/