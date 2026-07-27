package com.example.application.task.queries.models

data class PaginatedTasksQuery(
    val limit: Int,
    val page: Int,
)