package com.example.domain.events.interfaces

import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.presentation.dtos.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@JvmInline
value class EventId private constructor(@Serializable(with = UUIDSerializer::class) val value: UUID){
    companion object{
        fun generate(): EventId {
            return EventId(UUID.randomUUID())
        }

        fun create(id: String): Result<EventId, TaskError> { // Only for validation
            val validId = EventId(UUID.fromString(id))
            return Result.Success(validId)
        }

        fun fromDatabase(value: String): EventId { // For DB loading
            return EventId(UUID.fromString(value))
        }
    }
}