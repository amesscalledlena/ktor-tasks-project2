package com.example.domain.events.core

import com.example.presentation.dtos.serializers.InstantSerializer
import com.example.domain.events.interfaces.EventId
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.task.UserId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class TaskCompletedEvent(
    override val id: EventId = EventId.generate(),
    override val aggregateId: EventAggregateId,
    override val sequence: EventSequence,
    override val version: EventVersion = EventVersion(1),
    override val occurredByUserId: UserId,
    @Serializable(with = InstantSerializer::class)
    override val occurredOn: Instant = Instant.now()
) : TaskEvent{
    @SerialName("domain_event_type")
    override val type: EventType
        get() = EventType("TaskCompletedEvent")
}
