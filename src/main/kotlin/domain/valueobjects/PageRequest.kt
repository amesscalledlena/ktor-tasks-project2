package com.example.domain.valueobjects

class PageRequest(page: Int, limit: Int) {
    val safePage = if (page > 0) page else 1
    val safeLimit = if (limit in 1..100) limit else 10
    val offset : Long
        get() = ((safePage - 1) * safeLimit).toLong()

    fun calculateTotalPages(totalItems: Long): Int {
        return ((totalItems + safeLimit - 1) / safeLimit).toInt()
    }
}