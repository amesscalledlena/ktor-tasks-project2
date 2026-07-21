package com.example.domain.events

@JvmInline
value class EventType (val value: String) {
    init {
        require(value.isNotBlank()) { "Event type cannot be blank" }
    }

    companion object {

        fun constructFromPersisted(eventType: String): EventType {
            return EventType(eventType)
        }
    }

}