package com.example.domain.valueobjects

import com.example.presentation.dtos.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@kotlinx.serialization.Serializable
@JvmInline
value class UserId(
    @Serializable(with = UUIDSerializer::class) val value: UUID
)