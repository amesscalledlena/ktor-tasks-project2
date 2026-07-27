package com.example.application.step.commands

import com.example.domain.entities.TaskStep
import com.example.domain.interfaces.TaskStepRepository
import com.example.domain.railway.*
import com.example.domain.railway.Result.Companion.zip
import com.example.domain.valueobjects.step.TaskStepTitle
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CreateTaskStepHandler(private val repository: TaskStepRepository) {
    fun execute(command: CreateTaskStep): Result<Int, TaskError> {
        val titleVO = TaskStepTitle.create(command.title)

        return zip(
            a = titleVO,
            b = titleVO,
            failure = TaskError.InvalidTitle("Multiple validation errors occurred"),
            mapSuccess = { a, b ->
                val id = transaction {
                    val task = TaskStep.create(
                        title = titleVO.successOrException,
                    )
                    repository.saveStep(task)
                }
                id
            }
        )
    }
}
