package com.example.domain.valueobjects

import com.example.domain.railway.*
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class TaskTitle private constructor(val value: String){
    companion object{
        fun create(title: String): Result<TaskTitle, TaskError> {
            if (title.isBlank()){
                return Result.failure(TaskError.InvalidTitle("Title cannot be blank"))
            } else if (title.length >= 128){
                return Result.failure(TaskError.InvalidTitle("Title must be less than 128 characters long"))
            }else{
                val validTitle = TaskTitle(title)
                return Result.success(validTitle)
            }
        }

        fun fromDatabase(value: String): TaskTitle {
            return TaskTitle(value)
        }
    }
}