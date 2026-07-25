package com.example.application.queries.models

import com.example.domain.entities.Task
import com.example.presentation.dtos.TaskDto

data class PaginatedTasksResultQuery(
    val tasks: List<TaskDto>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int,
)