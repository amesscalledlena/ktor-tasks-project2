package com.example.domain.events.core

import com.example.domain.events.interfaces.Event
import com.example.domain.events.valueclasses.EventAggregateName

sealed interface TaskEvent: Event {
    override val aggregateName: EventAggregateName
        get() = EventAggregateName("Task") //Because every event that implements TaskEvent belongs to the Task aggregate.
}