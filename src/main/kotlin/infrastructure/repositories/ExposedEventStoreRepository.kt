package com.example.infrastructure.repositories

import com.example.domain.events.interfaces.Event
import com.example.domain.events.openclasses.EventAggregateId
import com.example.domain.interfaces.EventStoreRepository
import com.example.infrastructure.tables.EventStoreTbl
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

class ExposedEventStoreRepository : EventStoreRepository {

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
    }

    override fun getEventStream(aggregateId: EventAggregateId): List<Event> {
        return EventStoreTbl.selectAll().where { EventStoreTbl.aggregateId eq aggregateId.value }
            .orderBy(
                EventStoreTbl.sequence to SortOrder.ASC
            )
            .map { row ->
                val rawJson = row[EventStoreTbl.payload]
                Json.decodeFromString<Event>(rawJson)
            }
    }
}