package com.example.application.commands.models

data class CreateTaskCommand(
    val title: String,
    val description: String,
)