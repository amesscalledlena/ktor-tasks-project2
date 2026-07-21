package com.example.domain.events

@JvmInline
value class EventVersion (val value: Int) {
    init {
        require(value > 0) { "Event version cannot be negative" }
    }
}