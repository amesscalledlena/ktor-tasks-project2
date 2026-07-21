package com.example.domain.valueobjects

import com.example.domain.interfaces.EventId
import com.example.domain.railway.*
import java.util.UUID

@JvmInline
value class TaskId private constructor(override val eventId: UUID) : EventId { //private constructor forces to use the safe creation methods below, instead of randomly guessing UUIDs
    companion object{
        fun generate(): TaskId {
            return TaskId(UUID.randomUUID())
        }

        fun create(id: String): Result<TaskId, TaskError> { // Only for validation

            /*if(id<=0){
                return Result.failure(TaskError.InvalidId("ID must be greater than zero."))
            }else{
                val validId = TaskId(id)
                return Result.success(validId)
            }*/
        }

        fun fromDatabase(value: Int): TaskId { // For DB loading
            return TaskId(value)
        }

    }
}