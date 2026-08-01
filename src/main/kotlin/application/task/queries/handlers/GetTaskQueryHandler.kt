package com.example.application.task.queries.handlers

import com.example.application.task.queries.models.GetTaskQuery
import com.example.domain.interfaces.TaskRepository
import com.example.domain.railway.*
import com.example.domain.valueobjects.task.TaskId
import com.example.presentation.dtos.task.TaskDto
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class GetTaskQueryHandler(private val repository: TaskRepository) {
    fun execute(query: GetTaskQuery): Result<TaskDto, TaskError> {

        val idVO = when (val res = TaskId.create(query.id)) {
            is Result.Success -> res.value
            is Result.Failure -> return Result.failure(res.failure)
        }

        return transaction {
            val taskDto = repository.findById(idVO)

            if(taskDto == null) {
                Result.failure(TaskError.NotFound(idVO.value))
            } else {
                Result.success(taskDto)
            }
        }
    }
}
