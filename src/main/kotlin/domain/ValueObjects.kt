package com.example.domain

@JvmInline
value class TaskId(val value: Int){
    init{
        require(value >= 0) {"Data must be positive or zero"}
    }
}

@JvmInline
value class TaskTitle(val value: String){
    init {
        require(value.isNotBlank()) { "Title cannot be blank." }
        require(value.length < 128) { "Title should be 128 characters or less." }
    }
}

@JvmInline
value class TaskDescription(val value: String){
    init{
        require(!(value.isBlank())) { "Description cannot be blank." }
        require(value.length < 255) { "Description should be 255 characters or less." }
    }
}

