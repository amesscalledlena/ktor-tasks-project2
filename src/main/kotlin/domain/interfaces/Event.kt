package com.example.domain.interfaces

import com.example.domain.events.EventSequence
import java.time.Instant

interface Event {
    val aggregateName: EventAggregateName
    val type: EventType
    val aggregateId: EventAggregateId
    val sequence: EventSequence
    val version: EventVersion
    val occurredByUserId: UserId
    val occurredOn: Instant
    val id: EventId
}