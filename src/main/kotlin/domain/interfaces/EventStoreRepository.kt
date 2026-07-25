package com.example.domain.interfaces

import com.example.domain.events.interfaces.Event
import com.example.domain.events.openclasses.EventAggregateId

interface EventStoreRepository {
    fun append(event: Event): Int
    fun getEventStream(aggregateId: EventAggregateId): List<Event>
}