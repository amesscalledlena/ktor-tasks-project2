package com.example.domain.entities

import com.example.domain.interfaces.Event

abstract class EventSourceEntity<T : Event> {

    private val recordedEvents = mutableListOf<T>()
    fun getRecordedEvents(): List<T> = recordedEvents
    abstract fun apply(event: T)
    protected fun raiseEvent(event: T) {
        recordedEvents.add(event)
        apply(event)
    }

    protected fun applyEventStream(eventStream: List<T>) {
        eventStream.sortedBy { it.sequence.value }.forEach {
            apply(it)
            latestSequence = it.sequence
        }
    }
}