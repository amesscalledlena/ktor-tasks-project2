package com.example

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import java.time.Instant
import org.jetbrains.exposed.v1.javatime.timestamp

const val MAX_VARCHAR_LENGTH = 128

object Tasks: Table("Tasks") {
    val id = integer("id").autoIncrement()
    val title = varchar("name", MAX_VARCHAR_LENGTH)
    val description = varchar("description", MAX_VARCHAR_LENGTH)
    val isCompleted = bool("Completed").default(false)
    val createdAt: Column<Instant> = timestamp("created_at").clientDefault{ Instant.now() }
    val updatedAt: Column<Instant> = timestamp("updated_at").clientDefault{ Instant.now() }
}


// Before you start executing database operations, you must open a transaction.


