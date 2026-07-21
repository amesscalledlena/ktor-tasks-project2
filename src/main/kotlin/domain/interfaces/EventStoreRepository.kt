package com.example.domain.interfaces

interface EventStoreRepository {
    fun append(event: DomainEvent): Int
    fun getEventStream(taskId: Int): List<DomainEvent>
}