package com.example.application.queries

import com.example.domain.TaskRepository

class PaginatedTasksQueryHandler(private val repository: TaskRepository) {
    fun handle(query: PaginatedTasksQuery): PaginatedTasksResult {
        val safePage = if (query.page > 0) query.page else 1
        val safeLimit = if (query.limit in 1..100) query.limit else 10
        val offset =((safePage-1)*safeLimit).toLong()

        val totalItems = repository.count()
        val totalPages = ((totalItems + safeLimit - 1) / safeLimit).toInt()
        val tasks = repository.findAllPaginated(safeLimit, offset)

        return PaginatedTasksResult(tasks, totalItems, totalPages,safePage)
    }
}