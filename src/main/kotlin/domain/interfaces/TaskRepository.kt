package com.example.domain.interfaces

import com.example.domain.entities.Task
import com.example.domain.valueobjects.task.TaskId
import com.example.presentation.dtos.task.TaskDto

interface TaskRepository {
    fun save(task: Task)
    fun findById(id: TaskId): TaskDto?
    fun findAllPaginated(limit: Int, offset: Long): List<TaskDto>
    fun count(): Long
    fun delete(id: String): Boolean
}