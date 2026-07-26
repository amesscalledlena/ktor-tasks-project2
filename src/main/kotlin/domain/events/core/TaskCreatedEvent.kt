package com.example.domain.events.core

import com.example.domain.events.interfaces.EventId
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.*
import com.example.presentation.dtos.serializers.InstantSerializer
import com.example.presentation.dtos.serializers.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class TaskCreatedEvent(
    val taskId: EventId,
    val taskTitle: TaskTitle,
    val taskDescription: TaskDescription,
    @Serializable(with = UUIDSerializer::class)
    val eventId: UUID = UUID.randomUUID(),
    val taskPriority: TaskPriority,
    val taskCategory: TaskCategory,
    @Serializable(with = InstantSerializer::class)
    val taskDueDate: Instant?,
    @Serializable(with = InstantSerializer::class)
    override val occurredOn: Instant = Instant.now(),
    @SerialName("domain_event_type")
    override val type: EventType,
    override val aggregateId: EventAggregateId,
    override val sequence: EventSequence,
    override val version: EventVersion,
    override val occurredByUserId: UserId,
    override val id: EventId,
) : TaskEvent