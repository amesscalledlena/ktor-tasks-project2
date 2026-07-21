package com.example.infrastructure.repositories

import com.example.domain.interfaces.DomainEvent
import com.example.domain.interfaces.EventStoreRepository
import com.example.infrastructure.tables.EventStoreTbl
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.insert

class ExposedEventStoreRepository: EventStoreRepository {

    private val json = Json{ // Create a configured Json instance for the repository
        ignoreUnknownKeys = true
    }

    override fun append(event: DomainEvent): Int {
        val jsonPayload = json.encodeToString(event)

        val typeOfEvent = event::class.simpleName ?: "Unknown"

        return EventStoreTbl.insert {
            it[taskId] = event.taskId
            it[eventType] = typeOfEvent
            it[payload] = jsonPayload
            it[occurredOn] = event.occurredOn
        } get EventStoreTbl.id
        }

    override fun getEventStream(taskId: Int): List<DomainEvent> {
        TODO("Not yet implemented")
    }
}