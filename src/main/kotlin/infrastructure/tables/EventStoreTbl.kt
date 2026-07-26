package com.example.infrastructure.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp

object EventStoreTbl: Table(){
    val id = integer("id").autoIncrement()
    val eventId = varchar("event_id", 255)
    val aggregateId = varchar("aggregate_id", 255)
    val sequence = long("sequence")
    val eventType = varchar("event_type", 255)
    val occurredOn = timestamp("occurredOn")
    val payload = text("payload")

    override val primaryKey = PrimaryKey(id)
}