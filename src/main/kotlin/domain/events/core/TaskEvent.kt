package com.example.domain.events.core

import com.example.domain.events.valueclasses.EventAggregateName
import kotlinx.serialization.Serializable

@Serializable
sealed interface TaskEvent: Event {
    override val aggregateName: EventAggregateName
        get() = EventAggregateName("Task") //Because every event that implements TaskEvent belongs to the Task aggregate.
}