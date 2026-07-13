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


fun Application.configureRouting() {
    val taskRepository = ExposedTaskRepository()

    val createHandler = CreateTaskCommandHandler(taskRepository)
    val updateHandler = UpdateTaskCommandHandler(taskRepository)
    val deleteHandler = DeleteTaskCommandHandler(taskRepository)
    val getPaginatedHandler = PaginatedTasksQueryHandler(taskRepository)
    val getTaskHandler = GetTaskQueryHandler(taskRepository)
    val completeHandler = CompleteTaskCommandHandler(taskRepository)

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
                val allTasks = getPaginatedHandler.execute(query)
                val webResponse = PaginatedResponse(
                    data = allTasks.tasks.map { TaskResponse.fromDto(it) },
                    totalItems = allTasks.totalItems,
                    totalPages = allTasks.totalPages,
                    currentPage = allTasks.currentPage
                )

                call.respond(HttpStatusCode.OK, webResponse)
                //call.respond(HttpStatusCode.OK, allTasks)
            }
            //READ ONE
            get("/{id}") {
                val taskId = call.parameters["id"]!!.toInt()

                val query = GetTaskQuery(taskId)
                val task = getTaskHandler.execute(query)
                if (task != null) {
                    val webResponse = TaskResponse.fromDto(task)
                    call.respond(HttpStatusCode.OK, webResponse)
                } else {
                    call.respond(HttpStatusCode.NotFound)
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
                val updatedTask = updateHandler.execute(command)

                if (updatedTask) {
                    call.respond(HttpStatusCode.OK, updatedTask)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            patch("/complete"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest)
                val command = CompleteTaskCommand(taskId)
                val completedTask = completeHandler.execute(command)
                if (completedTask) {
                    call.respond(HttpStatusCode.OK, "Task marked as completed")
                }else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            //DELETE
            delete("/{id}") {
                val taskId =
                    call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val command = DeleteTaskCommand(taskId)
                val deletedTaskResult = deleteHandler.execute(command)
                if (deletedTaskResult) {
                    call.respond(HttpStatusCode.OK, deletedTaskResult)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
