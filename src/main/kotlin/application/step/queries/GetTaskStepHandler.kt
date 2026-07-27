package com.example.application.step.queries

import com.example.domain.entities.TaskStep
import com.example.domain.interfaces.TaskStepRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.step.TaskStepId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class GetTaskStepHandler (private val repository: TaskStepRepository) {
    fun execute(query: GetTaskStep): Result<TaskStep, TaskError> {
        val idVO = TaskStepId.create(query.id)

        if (idVO.isFailure) {
            return Result.failure(idVO.failureOrException)
        }

        return transaction {
            val taskStepId = idVO.successOrException
            val taskStep = repository.findStepById(taskStepId)

            if(taskStep==null){
                Result.failure(TaskError.NotFound(taskStepId.toString()))
            }else{
                Result.success(taskStep)
            }
        }
    }
}