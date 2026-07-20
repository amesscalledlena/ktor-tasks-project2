package com.example.domain.interfaces

interface EventBus {
    suspend fun publish(event: DomainEvent)
    //suspend fun <T: DomainEvent> subscribe(eventType: Class<T>, handler: suspend (T) -> Unit)
}