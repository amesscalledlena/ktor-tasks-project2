package com.example.domain.valueobjects

@JvmInline
value class TaskTitle(val value: String){
    init {
        require(value.isNotBlank()) { "Title cannot be blank." }
        require(value.length < 128) { "Title should be 128 characters or less." }
    }
    /*
    companion object{
        fun makeNew(value: String): TaskTitle {
            return TaskTitle(value.trim())
        }
    }

    data class Failer(val value: String){
        data class Invalid() : ResultR
    }
    */
}