package com.example.application.task.queries.models

data class PaginatedTasksQuery(
    val size: Int,
    val page: Int,
    val status: String? = null,
    val priority: String? = null,
    val category: String? = null,
)