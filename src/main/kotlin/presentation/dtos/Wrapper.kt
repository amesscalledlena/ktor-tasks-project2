package com.example.presentation.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse(
    val data: List<TaskResponse>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int
)
