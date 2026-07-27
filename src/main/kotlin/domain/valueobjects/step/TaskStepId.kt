package com.example.domain.valueobjects.step

import com.example.domain.railway.*

class TaskStepId private constructor(val value: Int) {
    companion object {
        fun create(id: Int): Result<TaskStepId, TaskError> { // Only for validation
            if(id<=0){
                return Result.failure(TaskError.InvalidId("ID must be greater than zero."))
            }else{
                val validId = TaskStepId(id)
                return Result.success(validId)
            }
        }

        fun fromDatabase(value: Int): TaskStepId { // For DB loading
            return TaskStepId(value)
        }
    }
}

/*package com.example.domain.valueobjects

import com.example.domain.railway.*

@JvmInline
value class TaskId private constructor(val value: Int){
    companion object{
        fun create(id: Int): Result<TaskId, TaskError> { // Only for validation
            if(id<=0){
                return Result.failure(TaskError.InvalidId("ID must be greater than zero."))
            }else{
                val validId = TaskId(id)
                return Result.success(validId)
            }
        }

        fun fromDatabase(value: Int): TaskId { // For DB loading
            return TaskId(value)
        }

    }
}*/