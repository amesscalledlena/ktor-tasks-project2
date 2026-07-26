package com.example.domain.entities

import com.example.domain.events.core.*
import com.example.domain.events.entity.EventSourceEntity
import com.example.domain.events.interfaces.EventId
import com.example.domain.events.valueclasses.EventSequence
import com.example.domain.events.valueclasses.EventType
import com.example.domain.events.valueclasses.EventVersion
import com.example.domain.railway.Result
import com.example.domain.railway.TaskError
import com.example.domain.valueobjects.*
import java.time.Instant

//A Task can only process task-related events, not user-related events.
//Why private constructor? Because state can only change because an event happened.
class Task private constructor() : EventSourceEntity<TaskEvent>() {
    //Properties must be mutable (var) internally so the apply() method can change them.
    //private set makes it so that outside classes can only read them, not change them.
    //Used lateinit because the private constructor starts completely empty.
    lateinit var id: TaskId
        private set // Public getter and private setter

    lateinit var title: TaskTitle
        private set

    lateinit var description: TaskDescription
        private set

    lateinit var updatedAt: Instant
        private set

    lateinit var createdAt: Instant
        private set

    lateinit var status: TaskStatus
        private set

    lateinit var priority: TaskPriority
        private set

    lateinit var category: TaskCategory
        private set

    var completedAt: Instant? = null
        private set

    var dueDate: Instant? = null
        private set

    private constructor(event: TaskEvent) : this() {
        raiseEvent(event)
    }

    /*private constructor(
        id: TaskId,
        title: TaskTitle,
        description: TaskDescription,
        userId: UserId,
        priority: TaskPriority,
        category: TaskCategory,
        dueDate: Instant?
    ) : this() {
        raiseEvent(
            TaskCreatedEvent(
                taskTitle = title,
                taskDescription = description,
                aggregateId = id,
                sequence = EventSequence(1),
                occurredByUserId = userId,
                taskId = EventId.fromDatabase(id.value),
                type = EventType("TaskCreatedEvent"),
                version = EventVersion(1),
                id = EventId.generate(),
                taskPriority = priority,
                taskCategory = category,
                taskDueDate = dueDate,
            )
        )
    }*/

    override fun apply(event: TaskEvent) { // The only way to change the state of a task
        when (event) {
            is TaskCompletedEvent -> {
                this.status = TaskStatus.DONE
                this.completedAt = event.occurredOn
            }

            is TaskCreatedEvent -> {
                apply(event)
            }

            is TaskUpdatedEvent -> {
                this.title = event.taskTitle
                this.description = event.taskDescription
                this.updatedAt = event.occurredOn
            }

            else -> {}
        }

    }

    companion object {
        fun makeNew(
            id: TaskId,
            title: TaskTitle,
            description: TaskDescription,
            userId: UserId,
            priority: TaskPriority,
            category: TaskCategory,
            dueDate: Instant?
        ): Result<Task, TaskError> {
            val initialEvent = TaskCreatedEvent(
                taskTitle = title,
                taskDescription = description,
                aggregateId = id,
                sequence = EventSequence(1),
                occurredByUserId = userId,
                taskId = EventId.fromDatabase(id.value),
                type = EventType("TaskCreatedEvent"),
                version = EventVersion(1),
                id = EventId.generate(),
                taskPriority = priority,
                taskCategory = category,
                taskDueDate = dueDate,
            )
            val task = Task(initialEvent)

            //The constructor handles calling raiseEvent() internally.
            //raiseEvent will add it to the 'uncommitted events' list to be saved to the database later,
            //and then it will instantly call the apply method to fill in the blank shell.

            return Result.Success(task)
        }

        fun fromDatabase(eventsList: List<TaskEvent>): Task {
            val task = Task()
            eventsList.forEach { pastEvent ->
                task.apply(pastEvent)
            }
            return task
        }
    }

    fun update(userId: UserId, title: TaskTitle, description: TaskDescription): Result<Task, TaskError> {
        val event = TaskUpdatedEvent(
            aggregateId = this.id,
            sequence = EventSequence(this.getRecordedEvents().size + 1L),
            occurredByUserId = userId,
            taskTitle = title,
            taskDescription = description,
        )

        raiseEvent(event)

        return Result.Success(this)
    }

    fun complete(userId: UserId): Result<Task, TaskError> {
        val event = TaskCompletedEvent(
            aggregateId = this.id,
            sequence = EventSequence(this.getRecordedEvents().size + 1L),
            occurredByUserId = userId,
        )

        raiseEvent(event)
        return Result.Success(this)
    }

    fun delete (userId: UserId): Result<Task, TaskError> {
        val event = TaskDeletedEvent(
            aggregateId = this.id,
            sequence = EventSequence(this.getRecordedEvents().size + 1L),
            occurredByUserId = userId,
            taskId = EventId.fromDatabase(this.id.value) ,
            type = EventType("TaskDeletedEvent"),
        )

        raiseEvent(event)
        return Result.Success(this)
    }

    private fun apply(event: TaskCreatedEvent) {
        this.id = event.aggregateId as TaskId
        this.title = event.taskTitle
        this.description = event.taskDescription
        this.updatedAt = event.occurredOn
        this.createdAt = event.occurredOn
        this.status = TaskStatus.TODO
        this.priority = event.taskPriority
        this.category = event.taskCategory
        this.dueDate = event.taskDueDate
    }
}
