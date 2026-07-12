package com.example.application.queries

import com.example.domain.TaskRepository
import com.example.domain.valueobjects.PageRequest

class PaginatedTasksQueryHandler(private val repository: TaskRepository) {
    fun execute(query: PaginatedTasksQuery): PaginatedTasksResult {
        val pageReq = PageRequest(query.page, query.limit)
        val totalItems = repository.count()
        val totalPages = pageReq.calculateTotalPages(totalItems)
        val currentPage = pageReq.safePage
        val tasks = repository.findAllPaginated(pageReq.safeLimit, pageReq.offset)

        return PaginatedTasksResult(tasks, totalItems, totalPages, currentPage)
    }
}