package com.example.infrastructure.repositories.task

import com.example.domain.entities.Task
import com.example.domain.events.core.Event
import com.example.domain.events.core.TaskEvent
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.interfaces.EventStoreRepository
import com.example.infrastructure.tables.EventStoreTbl
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class ExposedEventStoreRepository(private val taskReposiroty: ExposedTaskRepository) : EventStoreRepository {

    private val json = Json { // Create a configured Json instance for the repository
        ignoreUnknownKeys = true
    }

    override fun append(events: List<Event>) {
        for(event in events) {
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
        val aggId = events.last().aggregateId



        val taskCurrentStage = getEventStream(aggId)

        taskReposiroty.save(taskCurrentStage)

    }

    // TODO: event ro bedim be ye functioni ke bar assas noe event karesho anjam bede
    //TODO: Harbar stream ro nagirim, age create nabashe, stage akhar task ro bayad dashte bashim

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