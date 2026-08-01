package com.example.infrastructure.repositories.task

import com.example.domain.entities.Task
import com.example.domain.events.core.Event
import com.example.domain.events.core.TaskCompletedEvent
import com.example.domain.events.core.TaskCreatedEvent
import com.example.domain.events.core.TaskDeletedEvent
import com.example.domain.events.core.TaskEvent
import com.example.domain.events.core.TaskUpdatedEvent
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.interfaces.EventStoreRepository
import com.example.infrastructure.tables.EventStoreTbl
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class ExposedEventStoreRepository(private val taskRepository: ExposedTaskRepository) : EventStoreRepository {

    private val json = Json { // Create a configured Json instance for the repository
        ignoreUnknownKeys = true
    }

    override fun append(events: List<Event>) {
        for (event in events) {
            val jsonPayload = json.encodeToString(event)

            EventStoreTbl.insert {
                it[eventId] = event.id.value.toString()
                it[aggregateId] = event.aggregateId.value
                it[sequence] = event.sequence.value
                it[eventType] = event.type.value
                it[payload] = jsonPayload
                it[occurredOn] = event.occurredOn
            }
        }

        val taskEvents = events.filterIsInstance<TaskEvent>()
        if (taskEvents.isEmpty()) return

        helper(lastEvent = taskEvents.last(), newEvents = taskEvents)


    }

    private fun helper(lastEvent: TaskEvent, newEvents: List<TaskEvent>) {
        when (lastEvent) {
            is TaskCreatedEvent -> {
                val task = Task.fromDatabase(newEvents)
                taskRepository.save(task)
            }

            is TaskDeletedEvent -> {
                taskRepository.delete(lastEvent.aggregateId.value)
            }

            is TaskUpdatedEvent, is TaskCompletedEvent -> {
                val taskCurrentStage = getEventStream(lastEvent.aggregateId)
                taskRepository.save(taskCurrentStage)
            }
        }
    }

    override fun getEventStream(aggregateId: EventAggregateId): Task {
        val taskEvents = EventStoreTbl.selectAll().where { EventStoreTbl.aggregateId eq aggregateId.value }
            .orderBy(
                EventStoreTbl.sequence to SortOrder.ASC
            )
            .map { row ->
                val rawJson = row[EventStoreTbl.payload]
                Json.decodeFromString<Event>(rawJson) as TaskEvent
            }
        return Task.fromDatabase(taskEvents)
    }
}