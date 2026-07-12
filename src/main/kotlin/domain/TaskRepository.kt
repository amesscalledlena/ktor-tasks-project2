package com.example.domain

interface TaskRepository {
    fun insert(title: String, description: String, updatedAt: String): Int
    fun findById(id: Int): Task?
    fun findAllPaginated(limit: Int, offset: Long): List<Task>
    fun count(): Long
    fun update(id: Int, title: String, description: String, updatedAt: String, isCompleted: Boolean): Boolean
    fun delete(id: Int): Boolean
}