package com.example.domain.railway

interface ResultFailure {
    val message: String
    fun addCause(cause: ResultFailure){}
}