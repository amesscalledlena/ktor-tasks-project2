package com.example.plugins

import com.example.models.Task
import com.example.models.Tasks
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant

import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update


// CRUD for the tasks table, using endpoints

fun Application.configureRouting() {
    routing {
        route("/tasks"){
            //CREATE
            post{
                val newTask = call.receive<Task>()
                val createdTaskId = transaction{
                    Tasks.insert{
                        it[Tasks.title] = newTask.title
                        it[Tasks.description] = newTask.description
                        it[Tasks.updatedAt] = Instant.now().toString()
                        it[Tasks.isCompleted] = newTask.isCompleted
                    } get Tasks.id
                }
                call.respond(HttpStatusCode.Created, "created Task Id = $createdTaskId")
            }

            //READ ALL
            get{
                val tasks = transaction {
                    Tasks.selectAll().map { row ->
                        Task(
                            row[Tasks.id],
                            row[Tasks.title],
                            row[Tasks.description],
                            row[Tasks.updatedAt],
                            row[Tasks.isCompleted]
                        )
                    }
                }
                call.respond(HttpStatusCode.OK, tasks.toList())
            }
            //READ ONE
            get("{id}"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val task = transaction {
                    Tasks.selectAll().where{ Tasks.id eq taskId}.map { row ->
                        Task(
                            id = row[Tasks.id],
                            title = row[Tasks.title],
                            description = row[Tasks.description],
                            isCompleted = row[Tasks.isCompleted],
                            updatedAt = row [Tasks.updatedAt],
                        )
                    }.singleOrNull()
                }
                if (task != null) {
                    call.respond(HttpStatusCode.OK, task)
                } else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            //UPDATE
            put("{id}"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updatedTask = call.receive<Task>()
                val updatedRowCount = transaction{
                    Tasks.update({ Tasks.id eq taskId}){
                        it[Tasks.title] = updatedTask.title
                        it[Tasks.description] = updatedTask.description
                        it[Tasks.updatedAt] = Instant.now().toString()
                        it[Tasks.isCompleted] = updatedTask.isCompleted
                    }
                }

                if(updatedRowCount>0){
                    call.respond(HttpStatusCode.OK, updatedTask)
                }else{
                    call.respond(HttpStatusCode.NotFound)
            }
            }
            //DELETE
            delete ("{id}"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deletedRowCount = transaction{
                    Tasks.deleteWhere { Tasks.id eq taskId }
                }
                if(deletedRowCount > 0){
                    call.respond(HttpStatusCode.OK)
                }else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}