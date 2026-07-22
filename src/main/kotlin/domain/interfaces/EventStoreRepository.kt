package com.example.domain.interfaces

import com.example.domain.events.core.DomainEvent

interface EventStoreRepository {
    fun append(event: DomainEvent): Int
    fun getEventStream(taskId: Int): List<DomainEvent>
}