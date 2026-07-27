package com.example.domain.valueobjects.task

import com.example.domain.railway.*
import com.example.domain.railway.Result.Companion.success

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    companion object {
        fun create(value: String): Result<TaskPriority, TaskError> {
            val matchedPriority = entries.find{it.name.equals(value.trim(), ignoreCase = true)}

            return if (matchedPriority != null) {
                success(matchedPriority)
            }else{
                Result.failure(TaskError.InvalidPriority("Invalid priority level."))
            }
        }
    }
}