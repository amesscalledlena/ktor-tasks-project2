package com.example.presentation.plugins

import com.example.application.commands.handlers.CompleteTaskCommandHandler
import com.example.application.commands.models.CreateTaskCommand
import com.example.application.commands.handlers.CreateTaskCommandHandler
import com.example.application.commands.models.DeleteTaskCommand
import com.example.application.commands.handlers.DeleteTaskCommandHandler
import com.example.application.commands.models.UpdateTaskCommand
import com.example.application.commands.handlers.UpdateTaskCommandHandler
import com.example.application.commands.models.CompleteTaskCommand
import com.example.application.queries.models.GetTaskQuery
import com.example.application.queries.handlers.GetTaskQueryHandler
import com.example.application.queries.models.PaginatedTasksQuery
import com.example.application.queries.handlers.PaginatedTasksQueryHandler
import com.example.infrastructure.repositories.ExposedTaskRepository
import com.example.presentation.dtos.PaginatedResponse
import com.example.presentation.dtos.TaskResponse
import com.example.presentation.dtos.TaskUpdate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
    val taskRepository = ExposedTaskRepository()

    val completeHandler by inject<CompleteTaskCommandHandler>()
    val createHandler by inject<CreateTaskCommandHandler>()
    val deleteHandler by inject<DeleteTaskCommandHandler>()
    val updateHandler by inject<UpdateTaskCommandHandler>()
    val getPaginatedHandler by inject<PaginatedTasksQueryHandler>()
    val getTaskHandler by inject<GetTaskQueryHandler>()

    routing {
        swaggerUI(
            path = "swagger",
            swaggerFile = "openapi.yaml"
        ) //This will host your YAML file on a web interface at http://localhost:8080/swagger

        route("/tasks") {
            //CREATE
            post {
                val newTask = call.receive<TaskResponse>()
                val command = CreateTaskCommand(newTask.title, newTask.description)
                val newTaskId = createHandler.execute(command)
                call.respond(HttpStatusCode.Created, "Created new task with ID $newTaskId")
            }
            //READ ALL
            get {
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

                val query = PaginatedTasksQuery(limit = limit, page = page)
                val result = getPaginatedHandler.execute(query)

                result.onSuccess { paginatedResult ->
                    val response = PaginatedResponse(
                        data = paginatedResult.tasks.map { TaskResponse.fromDto(it) },
                        totalItems = paginatedResult.totalItems,
                        totalPages = paginatedResult.totalPages,
                        currentPage = paginatedResult.currentPage
                    )
                    call.respond(HttpStatusCode.OK, response)
                }.onFailure { exception ->
                    call.respond(HttpStatusCode.BadRequest, exception.message ?: "Failed to fetch tasks")
                }
            }
            //READ ONE
            get("/{id}") {
                val taskId = call.parameters["id"]!!.toInt()

                val query = GetTaskQuery(taskId)
                val result = getTaskHandler.execute(query)

                result.onSuccess { task ->
                    call.respond(HttpStatusCode.OK, TaskResponse.fromDto(task!!))
                }.onFailure { exception ->
                    call.respond(HttpStatusCode.NotFound, exception.message ?: "Task not found")
                }
            }
            //UPDATE
            put("/{id}") {
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updatedTaskData = call.receive<TaskUpdate>()
                val command = UpdateTaskCommand(
                    taskId,
                    updatedTaskData.title,
                    updatedTaskData.description,
                )
                val updatedTask = updateHandler.execute(command) // This is now a Result<boolean> wrapper

                updatedTask.onSuccess { isUpdated ->
                    if (isUpdated) {
                        call.respond(HttpStatusCode.OK, updatedTaskData)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }.onFailure { exception ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        exception.message ?: "Invalid update data"
                    ) // VO threw an error
                }

            }
            patch("/{id}/complete") {
                val taskId =
                    call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest)
                val command = CompleteTaskCommand(taskId)
                val result = completeHandler.execute(command)
                result.onSuccess { completed ->
                    if (completed) {
                        call.respond(HttpStatusCode.OK, "Task marked as completed")
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }.onFailure { exception ->
                    call.respond(HttpStatusCode.NotFound, exception.message ?: "Task not found")
                }
            }

            //DELETE
            delete("/{id}") {
                val taskId =
                    call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val command = DeleteTaskCommand(taskId)
                val result = deleteHandler.execute(command)
                result
                    .onSuccess { deleted ->
                        if (deleted) {
                            call.respond(HttpStatusCode.OK, "Task deleted successfully")
                        } else {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                    .onFailure { exception ->
                        call.respond(HttpStatusCode.BadRequest, exception.message ?: "Failed to delete task")
                    }
            }
        }
    }
}
