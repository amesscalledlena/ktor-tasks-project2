package com.example.domain.valueobjects

import com.example.domain.railway.Result
import com.example.domain.railway.TaskError

data class TaskCategory private constructor(val value: String) {
    companion object {
        fun create(value: String): Result<TaskCategory, TaskError> {
            val trimmed = value.trim()
            if (trimmed.isEmpty()) return Result.Failure(TaskError.InvalidDescription("Category cannot be blank"))

            return Result.Success(TaskCategory(trimmed))
        }

        fun fromDatabase(value: String): TaskCategory {
            return TaskCategory(value)
        }
    }
}