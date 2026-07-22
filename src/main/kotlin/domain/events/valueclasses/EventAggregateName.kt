package com.example.domain.events.valueclasses

@JvmInline
value class EventAggregateName (val value: String) {
    init {
        require(value.isNotBlank()) { "Event aggregate name cannot be blank" }
    }

    companion object {
        fun constructFromPersisted(aggregateName: String): EventAggregateName {
            return EventAggregateName(aggregateName)
        }
    }

}