package com.example.application.commands.models

data class DeleteTaskCommand(
    val id: String,
    val userId: String,
)