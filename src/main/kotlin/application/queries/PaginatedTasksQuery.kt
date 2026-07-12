package com.example.application.queries

data class PaginatedTasksQuery(
    val limit: Int,
    val page: Int,
)