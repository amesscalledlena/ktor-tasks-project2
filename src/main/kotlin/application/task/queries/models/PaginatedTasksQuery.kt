package com.example.application.task.queries.models

data class PaginatedTasksQuery(
    val size: Int,
    val page: Int,
)