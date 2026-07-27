package com.example.domain.interfaces

import com.example.domain.entities.TaskStep
import com.example.domain.valueobjects.step.TaskStepId

interface TaskStepRepository {
    fun saveStep(taskStep: TaskStep): Int
    fun findStepById(id: TaskStepId): TaskStep?
}