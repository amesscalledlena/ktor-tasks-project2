package com.example.presentation

import com.example.application.*
import com.example.application.commands.CreateTaskCommand
import com.example.application.commands.CreateTaskCommandHandler
import com.example.application.commands.DeleteTaskCommand
import com.example.application.commands.DeleteTaskCommandHandler
import com.example.application.commands.UpdateTaskCommand
import com.example.application.commands.UpdateTaskCommandHandler
import com.example.application.queries.GetTaskQuery
import com.example.application.queries.GetTaskQueryHandler
import com.example.application.queries.PaginatedTasksQuery
import com.example.application.queries.PaginatedTasksQueryHandler
import com.example.infrastructure.ExposedTaskRepository
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

    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi.yaml") //This will host your YAML file on a web interface at http://localhost:8080/swagger

        route("/tasks"){
            //CREATE
            post {
                val newTask = call.receive<TaskCreate>()
                val command = CreateTaskCommand(newTask.title, newTask.description)
                val newTaskId = createHandler.handle(command)
                call.respond(HttpStatusCode.Created, "Created new task with ID $newTaskId")
            }
            //READ ALL
            get{
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10

                val query = PaginatedTasksQuery(limit, page)
                val allTasks = getPaginatedHandler.handle(query)
                /*val webResponse = PaginatedResponse(
                    data = result.tasks.map { TaskCreate(it.title, it.description) },
                    totalItems = result.totalItems,
                    totalPages = result.totalPages,
                    currentPage = result.currentPage
                )

                call.respond(HttpStatusCode.OK, webResponse)*/
                call.respond(HttpStatusCode.OK, allTasks)
            }
            //READ ONE
            get("/{id}"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)

                val query = GetTaskQuery(taskId)
                val task = getTaskHandler.handle(query)
                if (task != null) {
                    call.respond(HttpStatusCode.OK, task)
                }else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            //UPDATE
            put("/{id}"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val updatedTaskData = call.receive<TaskUpdate>()
                val command = UpdateTaskCommand(
                    taskId,
                    updatedTaskData.title,
                    updatedTaskData.description,
                    updatedTaskData.isCompleted
                )
                val updatedTask = updateHandler.handle(command)

                if (updatedTask != null) {
                    call.respond(HttpStatusCode.OK, updatedTask)
                } else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            //DELETE
            delete ("/{id}"){
                val taskId = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val command = DeleteTaskCommand(taskId)
                val deletedTaskResult = deleteHandler.handle(command)
                if (deletedTaskResult != null) {
                    call.respond(HttpStatusCode.OK, deletedTaskResult)
                }else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}