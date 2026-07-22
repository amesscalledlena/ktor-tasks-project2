package com.example.domain.events.core

import com.example.domain.events.interfaces.EventId
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import com.example.domain.valueobjects.UserId
import java.time.Instant
import java.util.UUID

data class TaskCreatedEvent(
    val taskId: EventId,
    val taskTitle: TaskTitle,
    val taskDescription: TaskDescription,
    val eventId: UUID = UUID.randomUUID(),
    override val occurredOn: Instant = Instant.now(),
    override val type: EventType,
    override val aggregateId: EventAggregateId,
    override val sequence: EventSequence,
    override val version: EventVersion,
    override val occurredByUserId: UserId,
    override val id: EventId,
) : TaskEvent