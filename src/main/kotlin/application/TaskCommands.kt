package com.example.application
import com.example.domain.TaskRepository
import java.time.Instant

data class CreateTaskCommand(
    val title: String,
    val description: String,
)

class CreateTaskCommandHandler(private val repository: TaskRepository) {
    fun handle(command: CreateTaskCommand): Int {
        val updatedAt = Instant.now().toString()

        return repository.insert(command.title, command.description, updatedAt)
    }
}

data class UpdateTaskCommand(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

class  UpdateTaskCommandHandler(private val repository: TaskRepository) {
    fun handle(command: UpdateTaskCommand): Boolean {
        val updatedAt = Instant.now().toString()
        return repository.update(command.id, command.title, command.description, updatedAt, command.isCompleted)
    }
}

data class DeleteTaskCommand(
    val id: Int
)
class  DeleteTaskCommandHandler(private val repository: TaskRepository) {
    fun handle(command: DeleteTaskCommand): Boolean {
        return repository.delete(command.id)
    }
}