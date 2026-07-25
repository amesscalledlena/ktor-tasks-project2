package com.example.presentation.dtos

import java.time.Instant

data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val updatedAt: Instant
)