package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table

@Serializable
data class TaskCreate(
    val title: String,
    val description: String
)

data class TaskUpdate(
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

object TaskTbl : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description")
    val updatedAt = varchar("updatedAt", 255).nullable()
    val isCompleted = bool("isCompleted").nullable()

    override val primaryKey = PrimaryKey(id)
}