package com.example.domain.valueobjects

@JvmInline
value class TaskId(val value: Int){
    init{
        require(value >= 0) {"Data must be positive or zero"}
    }
}