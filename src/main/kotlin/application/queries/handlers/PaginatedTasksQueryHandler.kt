package com.example.application.queries.handlers

import com.example.application.queries.models.PaginatedTasksQuery
import com.example.application.queries.models.PaginatedTasksResultQuery
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.PageRequest
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PaginatedTasksQueryHandler(private val repository: TaskRepository) {
    fun execute(query: PaginatedTasksQuery): Result<PaginatedTasksResultQuery, TaskError> {
        val pageReqVO = PageRequest.create(page = query.page, limit = query.limit)

        if(pageReqVO is Result.Failure){
            return Result.failure(pageReqVO.failureOrException)
        }

        val pageReq = pageReqVO.successOrException

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

/*        return transaction {
            val result = runCatching {
                val pageReq = PageRequest(query.page, query.limit)
                val totalItems = repository.count()
                val totalPages = pageReq.calculateTotalPages(totalItems)
                val currentPage = pageReq.safePage
                val tasks = repository.findAllPaginated(pageReq.safeLimit, pageReq.offset)

                PaginatedTasksResultQuery(
                    tasks = tasks,
                    totalItems = totalItems,
                    totalPages = totalPages,
                    currentPage = currentPage
                )
            }
            result.onFailure {
                rollback()
            }
        }
*/