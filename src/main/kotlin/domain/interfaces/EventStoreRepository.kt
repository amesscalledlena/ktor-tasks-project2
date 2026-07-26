package com.example.domain.interfaces

import com.example.domain.entities.Task
import com.example.domain.events.core.Event
import com.example.domain.events.openclasses.EventAggregateId

interface EventStoreRepository {
    fun append(events: List<Event>)
    fun getEventStream(aggregateId: EventAggregateId): Task
}