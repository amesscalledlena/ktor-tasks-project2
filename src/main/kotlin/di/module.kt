package com.example.di

import com.example.application.commands.handlers.*
import com.example.application.queries.handlers.*
import com.example.domain.interfaces.*
import com.example.infrastructure.repositories.*
import org.koin.dsl.module

val infrastructureModule = module{
    // Whenever someone asks for the TaskRepository interface, give them a single, shared instance of ExposedTaskRepository
    single<TaskRepository> { ExposedTaskRepository() }
}

val applicationModule = module {
    // Commands
    single { CompleteTaskCommandHandler(
        repository = get(),
        eventStoreRepository = get()
    ) }
    single { CreateTaskCommandHandler(
        repository = get(),
        eventStoreRepository =  get()
    ) }
    single { DeleteTaskCommandHandler(
        repository = get(),
        eventStoreRepository = get()
    ) }
    single { UpdateTaskCommandHandler(
        repository = get(),
        eventStoreRepository = get()
    ) }
    // Queries
    single { GetTaskQueryHandler(repository = get()) }
    single{ PaginatedTasksQueryHandler(repository = get() ) }
}

val eventModule = module {
    single<EventStoreRepository> { ExposedEventStoreRepository() }
}