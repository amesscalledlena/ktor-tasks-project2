package com.example.presentation.plugins

import com.example.application.step.commands.CreateTaskStep
import com.example.application.step.commands.CreateTaskStepHandler
import com.example.application.step.queries.GetTaskStep
import com.example.application.step.queries.GetTaskStepHandler
import com.example.application.task.commands.handlers.CompleteTaskCommandHandler
import com.example.application.task.commands.handlers.CreateTaskCommandHandler
import com.example.application.task.commands.handlers.DeleteTaskCommandHandler
import com.example.application.task.commands.handlers.UpdateTaskCommandHandler
import com.example.application.task.commands.models.CompleteTaskCommand
import com.example.application.task.commands.models.CreateTaskCommand
import com.example.application.task.commands.models.DeleteTaskCommand
import com.example.application.task.commands.models.UpdateTaskCommand
import com.example.application.task.queries.handlers.GetTaskQueryHandler
import com.example.application.task.queries.handlers.PaginatedTasksQueryHandler
import com.example.application.task.queries.models.GetTaskQuery
import com.example.application.task.queries.models.PaginatedTasksQuery
import com.example.presentation.dtos.task.PaginatedResponse
import com.example.presentation.dtos.task.TaskResponse
import com.example.presentation.dtos.step.TaskStepRequest
import com.example.presentation.dtos.step.TaskStepResponse
import com.example.presentation.dtos.task.TaskUpdate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
    val completeHandler by inject<CompleteTaskCommandHandler>()
    val createHandler by inject<CreateTaskCommandHandler>()
    val deleteHandler by inject<DeleteTaskCommandHandler>()
    val updateHandler by inject<UpdateTaskCommandHandler>()
    val getPaginatedHandler by inject<PaginatedTasksQueryHandler>()
    val getTaskHandler by inject<GetTaskQueryHandler>()
    val createStepHandler by inject<CreateTaskStepHandler>()
    val getStepHandler by inject<GetTaskStepHandler>()

    //TODO: 1. UpdatedAt dar domain handle beshe. 2. Attribute limit beshe size va limit calculate beshe. 3. Read All ba option filter kardan.

    routing {
        swaggerUI(
            path = "swagger",
            swaggerFile = "openapi.yaml"
        ) //This will host the YAML file on a web interface at http://localhost:8080/swagger

        route("/tasks") {
            //CREATE
            post {
                val newTask = call.receive<TaskResponse>()
                val command = CreateTaskCommand(
                    newTask.    title, newTask.description,
                    userId = call.request.header("X-User-Id") ?: "00000000-0000-0000-0000-000000000000",
                    priority = newTask.priority ?: "MEDIUM",
                    category = newTask.category ?: "General"
                )
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
                    call.respond(HttpStatusCode.BadRequest, exception.message)
                }
            }
            //READ ONE
            get("/{id}") {
                val taskId = call.parameters["id"]!!

                val query = GetTaskQuery(taskId)
                val result = getTaskHandler.execute(query)

                result.onSuccess { task ->
                    call.respond(HttpStatusCode.OK, TaskResponse.fromDto(task))
                }.onFailure { exception ->
                    call.respond(HttpStatusCode.NotFound, exception.message)
                }
            }
            //UPDATE
            put("/{id}") {
                val taskId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updatedTaskData = call.receive<TaskUpdate>()
                val command = UpdateTaskCommand(
                    taskId,
                    updatedTaskData.title,
                    updatedTaskData.description,
                    userId = call.request.header("X-User-Id") ?: "00000000-0000-0000-0000-000000000000",
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
                        exception.message
                    ) // VO threw an error
                }

            }
            patch("/{id}/complete") {
                val taskId =
                    call.parameters["id"] ?: return@patch call.respond(HttpStatusCode.BadRequest)
                val command = CompleteTaskCommand(
                    taskId,
                    userId =call.request.header("X-User-Id") ?: "00000000-0000-0000-0000-000000000000",
                )
                val result = completeHandler.execute(command)
                result.onSuccess { completed ->
                    if (completed) {
                        call.respond(HttpStatusCode.OK, "Task marked as completed")
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }.onFailure { exception ->
                    call.respond(HttpStatusCode.NotFound, exception.message)
                }
            }

            //DELETE
            delete("/{id}") {
                val taskId =
                    call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val command = DeleteTaskCommand(
                    id = taskId,
                    userId = call.request.header("X-User-Id") ?: "00000000-0000-0000-0000-000000000000"
                )
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
                        call.respond(HttpStatusCode.BadRequest, exception.message)
                    }
            }
        }

        route("/task-steps") {

            // CREATE A STEP
            post {
                val request = call.receive<TaskStepRequest>()
                val command = CreateTaskStep(title = request.title)

                val result = createStepHandler.execute(command)

                result.onSuccess { newStepId ->
                    call.respond(HttpStatusCode.Created, "Created new step with ID $newStepId")
                }.onFailure { error ->
                    call.respond(HttpStatusCode.BadRequest, error.message)
                }
            }

            // GET A STEP
            get("/{id}") {
                val stepId = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Not a valid number")

                val query = GetTaskStep(id = stepId)
                val result = getStepHandler.execute(query)

                result.onSuccess { taskStep ->
                    call.respond(HttpStatusCode.OK, TaskStepResponse.fromDto(taskStep))
                }.onFailure { error ->
                    call.respond(HttpStatusCode.NotFound, error.message)
                }
            }
        }
    }
}
