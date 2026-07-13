package com.example

import com.example.infrastructure.tables.TaskTbl
import com.example.presentation.plugins.configureRouting
import com.example.presentation.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.module() {
    configureSerialization()
    configureRouting()
}
fun main(args : Array<String>) {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    //io.ktor.server.cio.EngineMain.main(args)

//
    transaction {
//        //addLogger(StdOutSqlLogger) //print sql to std-out
        SchemaUtils.create(TaskTbl) // Creates the tasks table
//
//        val taskId = TaskTbl.insert {
//            it[title]="Learn Exposed"
//            it[description]= "Go through the Get started with Exposed tutorial"
//        } get TaskTbl.id  //Because the insert() function returns an InsertStatement,
//                        // by using the get() method after the insert operation you retrieve the
//                        // autoincremented id value of the newly added row.
//
//        val secondTaskId = TaskTbl.insert {
//            it[title] = "Read The Hobbit"
//            it[description] = "Read the first two chapters of The Hobbit"
//            it[isCompleted] = true
//        } get TaskTbl.id
//
//        println("Created new tasks with ids $taskId and $secondTaskId.")
//
//        TaskTbl.select(TaskTbl.id.count(), TaskTbl.isCompleted).groupBy(TaskTbl.isCompleted).forEach {
//            println("${it[TaskTbl.isCompleted]}: ${it[TaskTbl.id.count()]}")
//        }
//
//        TaskTbl.update ({ TaskTbl.id eq taskId }){ //Returns the number of updated rows
//            it[isCompleted] = true
//            it[updatedAt] = Instant.now().toString()
//        }
//
//        val updatedTask = TaskTbl.select(TaskTbl.isCompleted).where(TaskTbl.id eq taskId).single()
//        // single() retrieves the first result found.
//        println("Updated task details: $updatedTask")
//
//        TaskTbl.deleteWhere { TaskTbl.id eq secondTaskId } // Returns the number of deleted rows
//        println("Remaining tasks: ${TaskTbl.selectAll().toList()}")
   }

    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

// 1. TODO: Fix the task created twice bug (Routing: Line 71) ✅
// 2. TODO: Clean up the packages ✅
// 3. TODO: Use complete from domain
// 4. TODO: Take transactions to use cases ✅
// 5. TODO: Read about railway and use it in the use cases