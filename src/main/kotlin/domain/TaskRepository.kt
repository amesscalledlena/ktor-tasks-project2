package com.example.domain

interface TaskRepository {
    fun save(task: Task): Int
    fun findById(id: Int): Task?
    fun findAllPaginated(limit: Int, offset: Long): List<Task>
    fun count(): Long
    fun update(task: Task): Boolean
    fun delete(id: Int): Boolean
}