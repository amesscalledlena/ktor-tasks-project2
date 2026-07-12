package com.example.domain

import java.time.Instant

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val updatedAt: Instant?,
    val isCompleted: Boolean?
)

