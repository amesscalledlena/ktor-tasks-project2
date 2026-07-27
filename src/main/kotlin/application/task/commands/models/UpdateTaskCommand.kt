package com.example.application.task.commands.models

data class UpdateTaskCommand(
    val id: String,
    val title: String,
    val description: String,
    val userId: String,
)