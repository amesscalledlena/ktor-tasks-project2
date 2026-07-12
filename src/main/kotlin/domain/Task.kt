package com.example.domain

import java.time.Instant

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val updatedAt: Instant?,
    val isCompleted: Boolean?
)

// 1. Define value objects to check the data in the domain BEFORE checking it in the DB and other layers
// 2. Take out the transactions and create use cases
// 3. Divide the commands and queries, even the data classes