package com.example.domain.events.core

import com.example.domain.events.interfaces.EventId
import com.example.domain.events.openclasses.EventAggregateId
import com.example.presentation.dtos.serializers.UUIDSerializer
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import com.example.domain.valueobjects.UserId
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class TaskUpdatedEvent (
    override val id: EventId = EventId(UUID.randomUUID()),
    override val aggregateId: EventAggregateId,
    override val sequence: EventSequence,
    override val version: EventVersion = EventVersion(1),
    override val occurredByUserId: UserId,
    @Serializable(with = InstantSerializer::class)
    override val occurredOn: Instant = Instant.now(),

    val taskTitle: TaskTitle,
    val taskDescription: TaskDescription,
) : TaskEvent{
    override val type: EventType
    get() = EventType("TaskUpdatedEvent")
}