package com.example.domain.entities

import com.example.domain.valueobjects.step.TaskStepId
import com.example.domain.valueobjects.step.TaskStepTitle

class TaskStep(
    var id: TaskStepId,
    var title: TaskStepTitle,
) {
    companion object {
        fun create(
            title: TaskStepTitle,
        ): TaskStep {
            return TaskStep(
                id = TaskStepId.fromDatabase(0),
                title = title,
            )
        }
    }
}
