package com.example.infrastructure.repositories

import com.example.domain.events.DomainEvent
import com.example.domain.interfaces.EventBus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class InMemoryEventBus: EventBus {
    // A Shared Flow broadcasts the exact same data to multiple listeners at the same time.
    private val _events = MutableSharedFlow<DomainEvent>(extraBufferCapacity = 64) // Mutable to receive data
    // extraBufferCapacity: If the listener is busy, keep up to 64 events in a temporary queue
    private val events = _events.asSharedFlow() // Exposed publicly and read-only

    override suspend fun publish(event: DomainEvent) {
        _events.emit(event)
    }

    /*@Suppress("UNCHECKED_CAST")
    override suspend fun <T : DomainEvent> subscribe(eventType: Class<T>, handler: suspend (T) -> Unit) {
        // collect: Keep listening forever
        // @Suppress("UNCHECKED_CAST"): Because we are using Java classes to check types at runtime,
            // Kotlin's strict type checker gets nervous. We know the cast to T is perfectly safe because filterIsInstance already verified it,
            // so we suppress the compiler warning.
        events.filterIsInstance(eventType.kotlin).collect { event -> handler(event as T) }
    }*/
}