package com.example.presentation.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val title: String,
    val description: String,
    val priority: String? = null,
    val category: String? = null,
) {
    companion object {
        fun fromDto(task: TaskDto) = TaskResponse(
            title = task.title,
            description = task.description,
            priority = task.priority,
            category = task.category,
        )
    }
}