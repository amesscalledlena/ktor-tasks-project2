package com.example.domain.valueobjects

import com.example.domain.railway.*

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    companion object {
        fun create(value: String): Result<TaskPriority, TaskError> {
            val matchedPriority = entries.find{it.name.equals(value.trim(), ignoreCase = true)}

            if (matchedPriority != null) {
                return Result.success(matchedPriority)
            }else{
                return Result.failure(TaskError.InvalidPriority("Invalid priority level."))
            }
        }
    }
}