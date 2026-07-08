package com.example

import com.example.Tasks
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

fun main() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    transaction {
        //addLogger(StdOutSqlLogger) //print sql to std-out
        SchemaUtils.create(Tasks) // Creates the tasks table

        val taskId = Tasks.insert {
            it[title]="Learn Exposed"
            it[description]= "Go through the Get started with Exposed tutorial"
        } get Tasks.id  //Because the insert() function returns an InsertStatement,
                        // by using the get() method after the insert operation you retrieve the
                        // autoincremented id value of the newly added row.

        val secondTaskId = Tasks.insert {
            it[title] = "Read The Hobbit"
            it[description] = "Read the first two chapters of The Hobbit"
            it[isCompleted] = true
        } get Tasks.id

        println("Created new tasks with ids $taskId and $secondTaskId.")

        Tasks.select(Tasks.id.count(), Tasks.isCompleted).groupBy(Tasks.isCompleted).forEach {
            println("${it[Tasks.isCompleted]}: ${it[Tasks.id.count()]}")
        }

        Tasks.update ({ Tasks.id eq taskId }){ //Returns the number of updated rows
            it[isCompleted] = true
        }

        val updatedTask = Tasks.select(Tasks.isCompleted).where(Tasks.id eq taskId).single()
        // single() retrieves the first result found.
        println("Updated task details: $updatedTask")

        Tasks.deleteWhere { Tasks.id eq secondTaskId } // Returns the number of deleted rows
        println("Remaining tasks: ${Tasks.selectAll().toList()}")
        rollback()
        commit()
    }
}
