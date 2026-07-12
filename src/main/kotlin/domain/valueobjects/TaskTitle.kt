package com.example.domain.valueobjects

@JvmInline
value class TaskTitle(val value: String){
    init {
        require(value.isNotBlank()) { "Title cannot be blank." }
        require(value.length < 128) { "Title should be 128 characters or less." }
    }
}