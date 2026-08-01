package com.example.presentation.dtos.task

import kotlinx.serialization.Serializable

@Serializable
data class TaskUpdate(
    val title: String,
    val description: String,
)