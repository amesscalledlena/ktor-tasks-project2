package com.example.domain.railway

sealed class TaskError: ResultFailure {
    data class NotFound(val taskId: Int) : TaskError(){
        override val message: String = "$taskId not found"
    }

    data class InvalidTitle(val reason: String) : TaskError(){
        override val message: String = reason
    }

    data class InvalidDescription(val reason: String) : TaskError(){
        override val message: String = reason
    }

    data class InvalidId(val reason: String) : TaskError(){
        override val message: String = reason
    }

    data class InvalidPagination(val reason: String) : TaskError() {
        override val message: String = reason
    }
}