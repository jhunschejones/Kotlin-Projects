package com.todo.list.controllers

import com.todo.list.entities.Tasks
import dev.alpas.http.HttpCall
import dev.alpas.orAbort
import dev.alpas.ozone.create
import dev.alpas.ozone.latest
import dev.alpas.ozone.update
import dev.alpas.routing.Controller
import dev.alpas.validation.min
import dev.alpas.validation.required
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.toList

class TaskController : Controller() {
    fun index(call: HttpCall) {
        val tasks = Tasks.latest().toList()
        val totalTasks = tasks.size
        val completedTasks = tasks.count { it.completed }

        call.render(
            "welcome",
            mapOf("tasks" to tasks, "totalTasks" to totalTasks, "completedTasks" to completedTasks)
        )
    }

    fun create(call: HttpCall) {
        call.applyRules("newTask") {
            required()
            min(2)
        }.validate()

        Tasks.create() {
            val taskName = call.stringParam("newTask")
            it.name to taskName
        }

        flash("success", "Successfully added to-do task")
        call.redirect().back()
    }

    fun update(call: HttpCall) {
        val taskIsCompleted = call.param("state") != null
        val taskId = call.longParam("id").orAbort()
        Tasks.update {
            it.completed to taskIsCompleted
            where { it.id eq taskId }
        }

        if (taskIsCompleted) {
            flash("success", "Successfully completed to-do task")
        } else {
            flash("success", "Successfully updated to-do task")
        }

        call.redirect().back()
    }

    fun delete(call: HttpCall) {
        val taskIdToDelete = call.longParam("id").orAbort()
        Tasks.delete { it.id eq taskIdToDelete }
        flash("success", "Successfully removed to-do task")
        call.redirect().back()
    }
}
