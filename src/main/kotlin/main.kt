package com.example

import com.example.di.applicationModule
import com.example.di.infrastructureModule
import com.example.presentation.plugins.configureRouting
import com.example.presentation.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.v1.jdbc.Database
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