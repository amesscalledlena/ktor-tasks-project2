package com.example.presentation.dtos

import java.time.Instant

data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val updatedAt: Instant,
    val createdAt: Instant,
    val dueDate: Instant?,
    val status: String,
    val priority: String,
    val category: String
)