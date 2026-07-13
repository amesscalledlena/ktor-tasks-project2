package com.example.application.queries.models

import com.example.domain.entities.Task

data class PaginatedTasksResultQuery(
    val tasks: List<Task>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int,
)