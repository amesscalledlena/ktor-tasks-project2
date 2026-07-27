package com.example.domain.valueobjects.task

import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserId private constructor(val value: String){
    companion object {
        fun generate(): UserId {
            return UserId(UUID.randomUUID().toString())
        }

        fun create(id: String): Result<UserId, TaskError> { // Only for validation
            val validId = UserId(UUID.fromString(id).toString())
            return Result.Success(validId)
        }

        fun fromDatabase(value: String): UserId { // For DB loading
            return UserId(value)
        }

    }
}