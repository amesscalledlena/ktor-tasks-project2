package com.example.domain.events.interfaces

import com.example.presentation.dtos.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@JvmInline
value class EventId(@Serializable(with = UUIDSerializer::class) val value: UUID)