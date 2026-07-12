package com.example.domain

import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import java.time.Instant

class Task(
    var id: TaskId,
    var title: TaskTitle,
    var description: TaskDescription,
    var updatedAt: Instant?,
    var isCompleted: Boolean?
) {
    companion object {
        fun create(
            title: TaskTitle,
            description: TaskDescription
        ): Task {
            return Task(
                id = TaskId(0),
                title = title,
                description = TaskDescription(description.toString()),
                updatedAt = Instant.now(),
                isCompleted = false,
            )
        }
    }

    fun complete(updatedAt: Instant) {
        this.isCompleted = true
        this.updatedAt = updatedAt
    }

    fun update(title: TaskTitle, description: TaskDescription,) {
        this.title = title
        this.description = description
    }
}

