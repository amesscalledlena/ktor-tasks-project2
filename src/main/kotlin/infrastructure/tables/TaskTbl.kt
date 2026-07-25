package com.example.infrastructure.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp

object TaskTbl : Table() {
    val id = varchar("id", 36)
    val title = varchar("title", 255)
    val description = text("description")
    val updatedAt = timestamp("updatedAt")
    val createdAt = timestamp("createdAt")
    val isCompleted = bool("isCompleted")

    override val primaryKey = PrimaryKey(id)
}