package com.example.application.commands

data class UpdateTaskCommand(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)