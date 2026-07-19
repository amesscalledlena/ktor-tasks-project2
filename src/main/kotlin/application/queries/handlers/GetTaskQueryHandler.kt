package com.example.application.queries.handlers

import com.example.application.queries.models.GetTaskQuery
import com.example.domain.entities.Task
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.TaskId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class GetTaskQueryHandler(private val repository: TaskRepository) {
    fun execute(query: GetTaskQuery): Result<Task, TaskError> {
        val idVO = TaskId.create(query.id)

        if (idVO.isFailure) {
            return Result.failure(idVO.failureOrException)
        }

        return transaction {
            val taskId = idVO.successOrException.value
            val task = repository.findById(taskId)

            if(task==null){
                Result.failure(TaskError.NotFound(taskId))
            }else{
                Result.success(task)
            }
        }
    }
}
