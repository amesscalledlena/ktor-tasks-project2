package com.example.domain.valueobjects

@JvmInline
value class TaskDescription(val value: String){
    init{
        require(!(value.isBlank())) { "Description cannot be blank." }
        require(value.length < 255) { "Description should be 255 characters or less." }
    }
}