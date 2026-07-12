package com.example.infrastructure

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.timestamp


object TaskTbl : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description")
    val updatedAt = timestamp("updatedAt")
    val createdAt = timestamp("createdAt")
    val isCompleted = bool("isCompleted")

    override val primaryKey = PrimaryKey(id)
}