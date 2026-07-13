package com.example.application.queries.models

data class PaginatedTasksQuery(
    val limit: Int,
    val page: Int,
)