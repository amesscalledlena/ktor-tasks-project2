package com.example.presentation

import kotlinx.serialization.Serializable

@Serializable
data class TaskCreate(
    val title: String,
    val description: String
)