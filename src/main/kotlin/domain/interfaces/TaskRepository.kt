package com.example.domain.interfaces

import com.example.domain.entities.Task
import com.example.domain.valueobjects.TaskId

interface TaskRepository {
    fun save(task: Task)
    fun findById(id: TaskId): Task?
    fun findAllPaginated(limit: Int, offset: Long): List<Task>
    fun count(): Long
    fun update(task: Task): Boolean
    fun delete(id: TaskId): Boolean
}