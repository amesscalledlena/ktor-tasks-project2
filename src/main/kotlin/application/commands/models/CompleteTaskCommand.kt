package com.example.application.commands.models

import com.example.domain.valueobjects.UserId


data class CompleteTaskCommand (
    val id: String,
    val userId: String
    )