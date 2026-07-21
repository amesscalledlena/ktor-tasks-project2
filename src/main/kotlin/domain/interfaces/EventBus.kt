package com.example.domain.interfaces

import com.example.domain.events.DomainEvent

interface EventBus {
    suspend fun publish(event: DomainEvent)
    //suspend fun <T: DomainEvent> subscribe(eventType: Class<T>, handler: suspend (T) -> Unit)
}