package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse(
    val data: List<TaskCreate>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int
)
