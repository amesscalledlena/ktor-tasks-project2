package com.example.domain.events.core

import com.example.domain.events.interfaces.EventId
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.events.valueclasses.EventAggregateName
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.UserId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
sealed interface Event {
    val aggregateName: EventAggregateName
    @SerialName("domain_event_type")
    val type: EventType
    val aggregateId: EventAggregateId
    val sequence: EventSequence
    val version: EventVersion
    val occurredByUserId: UserId
    val occurredOn: Instant
    val id: EventId
}