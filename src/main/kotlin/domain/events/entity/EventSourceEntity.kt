package com.example.domain.events.entity

import com.example.domain.events.interfaces.Event

abstract class EventSourceEntity<T : Event> {

    private val recordedEvents = mutableListOf<T>()

    var latestSequence: Long = 0
        protected set

    fun getRecordedEvents(): List<T> = recordedEvents

    abstract fun apply(event: T)

    protected fun raiseEvent(event: T) {
        recordedEvents.add(event)
        apply(event)
        latestSequence = event.sequence.value //Update the sequence when a new event happens
    }

    protected fun applyEventStream(eventStream: List<T>) {
        eventStream.sortedBy { it.sequence.value }.forEach {
            apply(it)
            latestSequence = it.sequence.value
        }
    }
}