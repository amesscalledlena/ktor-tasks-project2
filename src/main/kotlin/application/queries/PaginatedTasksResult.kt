package com.example.application.queries
import com.example.domain.Task

data class PaginatedTasksResult(
    val tasks: List<Task>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int,
)