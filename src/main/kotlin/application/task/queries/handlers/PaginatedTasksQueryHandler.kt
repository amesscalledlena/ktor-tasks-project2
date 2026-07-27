package com.example.application.task.queries.handlers

import com.example.application.task.queries.models.PaginatedTasksQuery
import com.example.application.task.queries.models.PaginatedTasksResultQuery
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.task.PageRequest
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PaginatedTasksQueryHandler(private val repository: TaskRepository) {
    fun execute(query: PaginatedTasksQuery): Result<PaginatedTasksResultQuery, TaskError> {
        val pageReq = when (val res = PageRequest.create(page = query.page, limit = query.limit)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        return transaction {
            val totalItems = repository.count()
            val tasks = repository.findAllPaginated(
                limit = pageReq.limit,
                offset = pageReq.offset
            )

            val resultQuery = PaginatedTasksResultQuery(
                tasks = tasks,
                totalItems = totalItems,
                totalPages = pageReq.calculateTotalPages(totalItems),
                currentPage = pageReq.page
            )

            Result.success(resultQuery)
        }
    }
}
