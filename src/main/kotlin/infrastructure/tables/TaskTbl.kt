package com.example.infrastructure.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.datetime
import org.jetbrains.exposed.v1.javatime.time
import org.jetbrains.exposed.v1.javatime.timestamp

object TaskTbl : Table() {
    val id = varchar("id", 36)
    val title = varchar("title", 255)
    val description = text("description")
    val updatedAt = timestamp("updatedAt")
    val createdAt = timestamp("createdAt")
    val dueDate = timestamp("dueDate").nullable()
    val status = varchar("status", 255)
    val priority = varchar("priority", 255)
    val category = varchar("category", 255)

    override val primaryKey = PrimaryKey(id)
}