package com.example.infrastructure.repositories.step

import com.example.domain.entities.TaskStep
import com.example.domain.interfaces.TaskStepRepository
import com.example.domain.valueobjects.step.TaskStepId
import com.example.domain.valueobjects.step.TaskStepTitle
import com.example.infrastructure.tables.TaskStepTbl
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class ExposedTaskStepRepository: TaskStepRepository {
    override fun saveStep(taskStep: TaskStep): Int {
        return TaskStepTbl.insert {
            it[TaskStepTbl.title] = taskStep.title.value
        } get TaskStepTbl.id
    }

    override fun findStepById(id: TaskStepId): TaskStep? {
        return TaskStepTbl.selectAll().where { TaskStepTbl.id eq id.value }.map { row ->
            TaskStep(
                id = TaskStepId.fromDatabase(row[TaskStepTbl.id]),
                title = TaskStepTitle.fromDatabase(row[TaskStepTbl.title]),
            )
        }.singleOrNull()
    }

}