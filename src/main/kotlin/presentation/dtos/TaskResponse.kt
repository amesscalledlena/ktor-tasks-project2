package com.example.presentation.dtos

import com.example.domain.entities.Task
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val title: String,
    val description: String,
) {
    companion object {
        fun fromDto(task: Task) = TaskResponse(
            title = task.title.value,
            description = task.description.value
        )
    }
}