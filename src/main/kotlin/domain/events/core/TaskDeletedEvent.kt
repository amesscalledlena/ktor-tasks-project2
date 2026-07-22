package com.example.domain.events.core

import com.example.domain.events.interfaces.EventId
import com.example.domain.events.openclasses.EventAggregateId
import com.example.presentation.dtos.serializers.UUIDSerializer
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.UserId
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class TaskDeletedEvent (
    val taskId: EventId,
    @Serializable(with = UUIDSerializer::class) val eventId: UUID = UUID.randomUUID(),
    @Serializable(with = InstantSerializer::class)
    override val occurredOn: Instant = Instant.now(),
    override val type: EventType,
    override val aggregateId: EventAggregateId,
    override val sequence: EventSequence,
    override val version: EventVersion,
    override val occurredByUserId: UserId,
    override val id: EventId,
): TaskEvent