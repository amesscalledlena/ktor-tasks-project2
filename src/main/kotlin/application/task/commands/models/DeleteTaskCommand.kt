package com.example.application.task.commands.models

data class DeleteTaskCommand(
    val id: String,
    val userId: String,
)