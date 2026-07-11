package com.example.presentation

import com.example.infrastructure.TaskTbl
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Instant
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.swagger.swaggerUI
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update


fun Application.configureRouting() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi.yaml") //This will host your YAML file on a web interface at http://localhost:8080/swagger

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
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

                val safePage = if (page > 0) page else 1
                val safelimit = if (limit in 1..100) limit else 10

                val offset =((safePage-1)*safelimit).toLong()

                val paginatedResult = transaction {
                    val totalItems = TaskTbl.selectAll().count()
                    val totalPages = ((totalItems + safelimit - 1) / safelimit).toInt()

                    val tasks = TaskTbl.selectAll()
                        .limit(safelimit)
                        .offset(offset)
                        .map { row ->
                        TaskCreate(
                            row[TaskTbl.title],
                            row[TaskTbl.description],
                        )
                        }
                    PaginatedResponse(
                        data=tasks,
                        totalPages = totalPages,
                        totalItems = totalItems,
                        currentPage = safePage
                    )
                }
                call.respond(HttpStatusCode.OK, paginatedResult)
            }
            //READ ONE
            get("/{id}"){
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
            put("/{id}"){
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
            delete ("/{id}"){
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