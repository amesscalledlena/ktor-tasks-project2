package com.example.application.commands

data class CreateTaskCommand(
    val title: String,
    val description: String,
)