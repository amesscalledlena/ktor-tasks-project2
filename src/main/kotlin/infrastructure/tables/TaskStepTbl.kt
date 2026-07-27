package com.example.infrastructure.tables

import org.jetbrains.exposed.v1.core.Table

object TaskStepTbl: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)

    override val primaryKey = PrimaryKey(id)
}