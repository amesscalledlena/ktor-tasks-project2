package com.example.domain.valueobjects.step

import com.example.domain.railway.*

class TaskStepTitle private constructor(val value: String) {
    companion object {
        fun create(title: String): Result<TaskStepTitle, TaskError> {
            if (title.isBlank()){
                return Result.failure(TaskError.InvalidTitle("Title cannot be blank"))
            } else if (title.length >= 128){
                return Result.failure(TaskError.InvalidTitle("Title must be less than 128 characters long"))
            }else{
                val validTitle = TaskStepTitle(title)
                return Result.success(validTitle)
            }
        }

        fun fromDatabase(value: String): TaskStepTitle {
            return TaskStepTitle(value)
        }
    }
}
