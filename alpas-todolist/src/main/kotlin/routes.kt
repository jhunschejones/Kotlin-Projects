package com.todo.list

import com.todo.list.controllers.TaskController
import com.todo.list.controllers.WelcomeController
import dev.alpas.routing.RouteGroup
import dev.alpas.routing.Router

// https://alpas.dev/docs/routing
fun Router.addRoutes() = apply {
    group {
        webRoutesGroup()
    }.middlewareGroup("web")

    apiRoutes()
}

private fun RouteGroup.webRoutesGroup() {
    get("/", TaskController::index).name("welcome")
    post("/", TaskController::create).name("create")
    patch("/", TaskController::update).name("update")
    delete("/", TaskController::delete).name("delete")
}

private fun Router.apiRoutes() {
    // register API routes here
}
