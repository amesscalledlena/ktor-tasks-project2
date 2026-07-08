package com.example.plugins

import com.example.models.TaskCreate
import com.example.models.TaskTbl
import com.example.models.TaskUpdate
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




fun Application.configureRouting() {
    routing {
        route("/tasks"){
            //CREATE
            post{
                val newTaskCreate = call.receive<TaskCreate>()
                val createdTaskId = transaction{
                    TaskTbl.insert{
                        it[TaskTbl.title] = newTaskCreate.title
                        it[TaskTbl.description] = newTaskCreate.description
                        it[TaskTbl.updatedAt] = Instant.now().toString()
                    } get TaskTbl.id
                }
                call.respond(HttpStatusCode.Created, "created Task Id = $createdTaskId")
            }

            //READ ALL
            get{
                val taskCreates = transaction {
                    TaskTbl.selectAll().map { row ->
                        TaskCreate(
                            row[TaskTbl.title],
                            row[TaskTbl.description],
                        )
                    }
                }
                call.respond(HttpStatusCode.OK, taskCreates.toList())
            }
            //READ ONE
            get("/id"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val taskCreate = transaction {
                    TaskTbl.selectAll().where{ TaskTbl.id eq taskId}.map { row ->
                        TaskCreate(
                            title = row[TaskTbl.title],
                            description = row[TaskTbl.description],
                        )
                    }.singleOrNull()
                }
                if (taskCreate != null) {
                    call.respond(HttpStatusCode.OK, taskCreate)
                } else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            //UPDATE
            put("/id"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updatedTaskCreate = call.receive<TaskUpdate>()
                val updatedRowCount = transaction{
                    TaskTbl.update({ TaskTbl.id eq taskId}){
                        it[TaskTbl.title] = updatedTaskCreate.title
                        it[TaskTbl.description] = updatedTaskCreate.description
                        it[TaskTbl.updatedAt] = Instant.now().toString()
                        it[TaskTbl.isCompleted] = updatedTaskCreate.isCompleted
                    }
                }

                if(updatedRowCount>0){
                    call.respond(HttpStatusCode.OK, updatedTaskCreate)
                }else{
                    call.respond(HttpStatusCode.NotFound)
            }
            }

            //DELETE
            delete ("/id"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deletedRowCount = transaction{
                    TaskTbl.deleteWhere { TaskTbl.id eq taskId }
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