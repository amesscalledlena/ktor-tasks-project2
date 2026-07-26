package com.example.domain.events.core

import com.example.domain.events.interfaces.EventId
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.*
import java.time.Instant
import java.util.*

data class TaskCreatedEvent(
    val taskId: EventId,
    val taskTitle: TaskTitle,
    val taskDescription: TaskDescription,
    val eventId: UUID = UUID.randomUUID(),
    val taskPriority: TaskPriority,
    val taskCategory: TaskCategory,
    val taskDueDate: Instant?,
    override val occurredOn: Instant = Instant.now(),
    override val type: EventType,
    override val aggregateId: EventAggregateId,
    override val sequence: EventSequence,
    override val version: EventVersion,
    override val occurredByUserId: UserId,
    override val id: EventId,
) : TaskEvent