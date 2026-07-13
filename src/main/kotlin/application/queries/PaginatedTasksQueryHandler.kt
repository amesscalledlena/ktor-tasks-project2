package com.example.application.queries

import com.example.domain.TaskRepository
import com.example.domain.valueobjects.PageRequest
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PaginatedTasksQueryHandler(private val repository: TaskRepository) {
    fun execute(query: PaginatedTasksQuery): PaginatedTasksResult {
        return transaction {
            val pageReq = PageRequest(query.page, query.limit)
            val totalItems = repository.count()
            val totalPages = pageReq.calculateTotalPages(totalItems)
            val currentPage = pageReq.safePage
            val tasks = repository.findAllPaginated(pageReq.safeLimit, pageReq.offset)

            PaginatedTasksResult(
                tasks = tasks,
                totalItems = totalItems,
                totalPages = totalPages,
                currentPage = currentPage
            )
        }
    }
}