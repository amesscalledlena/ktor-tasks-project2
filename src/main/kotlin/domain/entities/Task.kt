package com.example.domain.entities

import com.example.domain.events.core.*
import com.example.domain.events.entity.EventSourceEntity
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.TaskDescription
import com.example.domain.valueobjects.TaskId
import com.example.domain.valueobjects.TaskTitle
import java.time.Instant
import com.example.domain.railway.*
import com.example.domain.valueobjects.UserId

//A Task can only process task-related events, not user-related events.
//Why private constructor? Because state can only change because an event happened.
class Task private constructor() : EventSourceEntity<TaskEvent>() {
    //Properties must be mutable (var) internally so the apply() method can change them.
    //private set makes it so that outside classes can only read them, not change them.
    //Used lateinit because the private constructor starts completely empty.
    lateinit var id: TaskId
        private set // Public getter and private setter

    lateinit var title: TaskTitle // TODO()
        private set

    var description: TaskDescription? = null
        private set

    lateinit var updatedAt: Instant
        private set

    var isCompleted: Boolean = false
        private set

    override fun apply(event: TaskEvent) { // The only way to change the state of a task
        when(event){
            is TaskCompletedEvent -> { this.isCompleted = true }
            is TaskCreatedEvent -> {
                this.id = event.aggregateId as TaskId
                this.title = event.taskTitle
                this.description = event.taskDescription
                this.updatedAt = event.occurredOn
                this.isCompleted = false
            }
            is TaskDeletedEvent -> {}
            is TaskUpdatedEvent -> {
                this.title = event.taskTitle
                this.description = event.taskDescription
                this.updatedAt = event.occurredOn
            }
        }

    }

    companion object {
        fun makeNew(event: TaskEvent): Result<Task, TaskError> {
            val task = Task()
            task.raiseEvent(event) //TODO()
            //raiseEvent will add it to the 'uncommitted events' list to be saved to the database later,
            //and then it will instantly call the apply method to fill in the blank shell.
            return Result.Success(task)
        }
    }

    fun update(userId: UserId, title: TaskTitle, description: TaskDescription): Result<Task, TaskError> {
        val event = TaskUpdatedEvent(
            aggregateId = this.id,
            sequence = EventSequence(this.getRecordedEvents().size +1L),
            occurredByUserId=userId,
            taskTitle = title,
            taskDescription = description,
        )

        raiseEvent(event)

        return Result.Success(this)
    }

    fun complete(userId: UserId): Result<Task, TaskError> {
        val event = TaskCompletedEvent(
            aggregateId = this.id,
            sequence = EventSequence(this.getRecordedEvents().size +1L),
            occurredByUserId = userId,
        )

        raiseEvent(event)
        return Result.Success(this)
    }
}
