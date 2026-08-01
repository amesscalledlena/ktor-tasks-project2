package com.example.application.task.queries.models

import com.example.presentation.dtos.task.TaskDto

data class PaginatedTasksResultQuery(
    val tasks: List<TaskDto>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int,
)