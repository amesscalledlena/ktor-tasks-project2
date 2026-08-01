package com.example.application.task.queries.handlers

import com.example.application.task.queries.models.PaginatedTasksQuery
import com.example.application.task.queries.models.PaginatedTasksResultQuery
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.task.PageRequest
import com.example.domain.valueobjects.task.TaskCategory
import com.example.domain.valueobjects.task.TaskPriority
import com.example.domain.valueobjects.task.TaskStatus
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PaginatedTasksQueryHandler(private val repository: TaskRepository) {
    fun execute(query: PaginatedTasksQuery): Result<PaginatedTasksResultQuery, TaskError> {
        val pageReq = when (val res = PageRequest.create(page = query.page, limit = query.size)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        // .entries.find is used for enums and never throws exceptions.
        val statusFilter = query.status?.let { rawStatus ->
            TaskStatus.entries.find { it.name == rawStatus }
                ?: return Result.failure(TaskError.InvalidTitle("Invalid status filter: $rawStatus"))
        }

        val priorityFilter = query.priority?.let { rawPriority ->
            TaskPriority.entries.find { it.name == rawPriority }
                ?: return Result.failure(TaskError.InvalidTitle("Invalid priority filter: $rawPriority"))
        }

        val categoryFilter = query.category?.let { rawCategory ->
            when (val res = TaskCategory.create(rawCategory)) {
                is Result.Success -> res.value
                is Result.Failure -> return Result.failure(res.failure)
            }
        }

        return transaction {
            val totalItems = repository.count(
                status = statusFilter,
                priority = priorityFilter,
                category = categoryFilter,
            )
            val tasks = repository.findAllPaginated(
                limit = pageReq.limit,
                offset = pageReq.offset,
                status = statusFilter,
                priority = priorityFilter,
                category = categoryFilter,
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
