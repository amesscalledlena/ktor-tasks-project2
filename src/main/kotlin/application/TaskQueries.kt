package com.example.application

import com.example.domain.Task
import com.example.domain.TaskRepository
import com.example.presentation.TaskCreate

data class PaginatedTasksQuery(
    val limit: Int,
    val page: Int,
)

data class PaginatedTasksResult(
    val tasks: List<Task>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPage: Int,
)

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

data class GetTaskQuery(val id: Int)

class GetTaskQueryHandler(private val repository: TaskRepository) {
    fun handle(query: GetTaskQuery): Task? {
        return repository.findById(query.id)
    }
}