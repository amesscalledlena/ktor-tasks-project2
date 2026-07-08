package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table

@Serializable
data class Task(
    val id: Int? = null,
    val title: String,
    val description: String,
    val updatedAt: String?,
    val isCompleted: Boolean
)

object Tasks : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description")
    val updatedAt = varchar("updatedAt", 255)
    val isCompleted = bool("isCompleted")

    override val primaryKey = PrimaryKey(id)
}