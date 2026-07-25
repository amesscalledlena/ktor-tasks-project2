package com.example.application.commands.models

import java.time.Instant


data class CreateTaskCommand(
    val title: String,
    val description: String,
    val userId: String,
    val priority: String,
    val category: String,
    val dueDate: Instant? = null
)