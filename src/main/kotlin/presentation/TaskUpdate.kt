package com.example.presentation

import kotlinx.serialization.Serializable

@Serializable
data class TaskUpdate(
    val title: String,
    val description: String,
    val isCompleted: Boolean
)