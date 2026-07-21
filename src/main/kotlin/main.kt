package com.example

import com.example.di.*
import com.example.infrastructure.tables.TaskTbl
import com.example.presentation.plugins.configureRouting
import com.example.presentation.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.module() {
    // Install Koin
    install(Koin) {
        slf4jLogger() // Uses Ktor's standard logger
        modules(infrastructureModule, applicationModule)
    }
    configureSerialization()
    configureRouting()
}
fun main(args : Array<String>) {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

// 1. TODO: Double task creation (Routing: Line 71) ✅
// 2. TODO: Clean up the packages ✅
// 3. TODO: Use complete from domain ✅
// 4. TODO: Take transactions to use cases ✅
// 5. TODO: Read about railway and use it in the use cases ✅
// 6. TODO: Clean architecture book (jeldesh lacivert bood)
// 7. TODO: DDD book
// 8. TODO: Event saving format + Add JSON column
// 9. TODO: Add more events if needed
// 10. TODO: Learn about class/interface generics in kotlin
