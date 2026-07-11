package com.example.infrastructure

import org.jetbrains.exposed.v1.core.Table


object TaskTbl : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val description = text("description")
    val updatedAt = varchar("updatedAt", 255).nullable()
    val isCompleted = bool("isCompleted").nullable()

    override val primaryKey = PrimaryKey(id)
}