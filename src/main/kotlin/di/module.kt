package com.example.di

import com.example.application.task.commands.handlers.CompleteTaskCommandHandler
import com.example.application.task.commands.handlers.CreateTaskCommandHandler
import com.example.application.task.commands.handlers.DeleteTaskCommandHandler
import com.example.application.task.commands.handlers.UpdateTaskCommandHandler
import com.example.application.task.queries.handlers.GetTaskQueryHandler
import com.example.application.task.queries.handlers.PaginatedTasksQueryHandler
import com.example.domain.interfaces.*
import com.example.infrastructure.repositories.step.ExposedTaskStepRepository
import com.example.infrastructure.repositories.task.ExposedEventStoreRepository
import com.example.infrastructure.repositories.task.ExposedTaskRepository
import org.koin.dsl.module

val infrastructureModule = module{
    // Whenever someone asks for the TaskRepository interface, give them a single, shared instance of ExposedTaskRepository
    single<TaskRepository> { ExposedTaskRepository() }

    single<EventStoreRepository> { ExposedEventStoreRepository(get()) }

    single<TaskStepRepository> { ExposedTaskStepRepository() }
}

val applicationModule = module {
    // Commands
    single {
        CompleteTaskCommandHandler(
            repository = get(),
            eventStoreRepository = get()
        )
    }
    single {
        CreateTaskCommandHandler(
            repository = get(),
            eventStoreRepository = get()
        )
    }
    single {
        DeleteTaskCommandHandler(
            repository = get(),
            eventStoreRepository = get()
        )
    }
    single {
        UpdateTaskCommandHandler(
            repository = get(),
            eventStoreRepository = get()
        )
    }
    // Queries
    single { GetTaskQueryHandler(repository = get()) }
    single{ PaginatedTasksQueryHandler(repository = get()) }
}
