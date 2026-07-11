package com.example.domain

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val updatedAt: String?,
    val isCompleted: Boolean?
)