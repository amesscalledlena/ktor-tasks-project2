package com.example.domain.valueobjects

import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import java.util.*

data class TaskId private constructor(
    override val value: String) : EventAggregateId(value) { //private constructor forces to use the safe creation methods below, instead of randomly guessing UUIDs
    companion object{
        fun generate(): TaskId {
            return TaskId(UUID.randomUUID().toString())
        }

        fun create(id: String): Result<TaskId, TaskError> { // Only for validation
            val validId = TaskId(UUID.fromString(id).toString())
            return Result.Success(validId)
        }

        fun fromDatabase(value: UUID): TaskId { // For DB loading
            return TaskId(value.toString())
        }

    }
}