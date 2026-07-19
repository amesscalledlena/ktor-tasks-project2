package com.example.domain.valueobjects

import com.example.domain.railway.Result
import com.example.domain.railway.TaskError

@JvmInline
value class TaskDescription private constructor(val value: String) {
    companion object {
        fun create(description: String): Result<TaskDescription, TaskError> {
            if(description.isBlank()){
                return Result.failure(TaskError.InvalidDescription("Description cannot be blank"))
            }else if(description.length > 255){
                return Result.failure(TaskError.InvalidDescription("Length of task description should be less than 255 characters"))
            }else{
                val validDescription = TaskDescription(description)
                return Result.success(validDescription)
            }
        }

        fun fromDatabase(value: String): TaskDescription {
            return TaskDescription(value)
        }
    }
}