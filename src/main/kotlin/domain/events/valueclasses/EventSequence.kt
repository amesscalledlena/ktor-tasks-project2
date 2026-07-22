package com.example.domain.events.valueclasses

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class EventSequence (val value: Long) {
    init {
        require(value >= 0) { "Event sequence cannot be negative" }
    }

    companion object {
        fun new(): EventSequence {
            return EventSequence(0)
        }

        fun generate(lastSequence: EventSequence): EventSequence {
            return EventSequence(lastSequence.value + 1)
        }

        fun constructFromPersisted(sequence: Long): EventSequence {
            return EventSequence(sequence)
        }
    }

    internal fun increment() = generate(this)

}