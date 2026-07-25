package com.example.domain.interfaces

import com.example.domain.events.interfaces.Event
import com.example.domain.events.openclasses.EventAggregateId

interface EventStoreRepository {
    fun append(events: List<Event>): Unit
    fun getEventStream(aggregateId: EventAggregateId): List<Event>
}