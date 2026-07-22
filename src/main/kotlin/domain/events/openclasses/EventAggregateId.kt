package com.example.domain.events.openclasses

import kotlinx.serialization.Serializable

@Serializable
open class EventAggregateId(open val value:String)