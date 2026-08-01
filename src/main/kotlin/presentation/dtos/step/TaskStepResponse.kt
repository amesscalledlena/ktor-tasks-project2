package com.example.presentation.dtos.step

import com.example.domain.entities.TaskStep
import kotlinx.serialization.Serializable

@Serializable
data class TaskStepResponse(
    val title: String,
) {
    companion object {
        fun fromDto(task: TaskStep) = TaskStepResponse(
            title = task.title.toString(),
        )
    }
}