package com.example.application.commands.models

data class UpdateTaskCommand(
    val id: Int,
    val title: String,
    val description: String,
)