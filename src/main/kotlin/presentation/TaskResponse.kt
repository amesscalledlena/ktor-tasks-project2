package com.example.presentation

import com.example.domain.Task
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    val title: String,
    val description: String,
) {
    companion object {
        fun fromDto(row: Task) = TaskResponse(
            title = row.title.value,
            description = row.description.value
        )
    }
}