package com.example.domain.events.interfaces

import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.events.valueclasses.EventAggregateName
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.valueobjects.UserId
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