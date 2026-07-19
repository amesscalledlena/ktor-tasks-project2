package com.example.domain.valueobjects

import com.example.domain.railway.*

data class PageRequest private constructor(val page: Int,val limit: Int) {
    val offset: Long
        get() = ((page - 1) * limit).toLong()

    fun calculateTotalPages(totalItems: Long): Int{
        return ((totalItems + limit - 1) / limit).toInt()
    }
    companion object {
        fun create(page: Int, limit: Int): Result<PageRequest, TaskError> {
            if(page < 1){
                return Result.failure(TaskError.InvalidPagination("Page must be greater than 0"))
            }else if (limit < 1){
                return Result.failure(TaskError.InvalidPagination("Limit must be greater than 0"))
            }else if (limit > 100){
                return Result.failure(TaskError.InvalidPagination("Limit must be less than 100"))
            }else{
                val validPagination = PageRequest(page, limit)
                return Result.success(validPagination)
            }
        }
    }
}

/*
    val safePage = if (page > 0) page else 1
    val safeLimit = if (limit in 1..100) limit else 10
    val offset : Long
        get() = ((safePage - 1) * safeLimit).toLong()

    fun calculateTotalPages(totalItems: Long): Int {
        return ((totalItems + safeLimit - 1) / safeLimit).toInt()
    }
*/