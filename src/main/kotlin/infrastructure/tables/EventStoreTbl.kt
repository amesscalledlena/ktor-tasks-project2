package com.example.infrastructure.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp

object EventStoreTbl: Table(){
    val id = integer("id").autoIncrement()
    val taskId = integer("task_id")
    val eventType = varchar("eventType", 255)
    val occurredOn = timestamp("occurredOn")
    val payload = text("payload")

    override val primaryKey = PrimaryKey(EventStoreTbl.id)
}