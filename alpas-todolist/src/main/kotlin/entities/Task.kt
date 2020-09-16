package com.todo.list.entities

import dev.alpas.ozone.*
import me.liuwj.ktorm.schema.boolean
import java.time.Instant

interface Task : OzoneEntity<Task> {
    var id: Long
    var name: String?
    var createdAt: Instant?
    var updatedAt: Instant?
    val completed: Boolean

    companion object : OzoneEntity.Of<Task>()
}

object Tasks : OzoneTable<Task>("tasks") {
    val id by bigIncrements()
    val name by string("name").size(150).nullable().bindTo { it.name }
    val createdAt by createdAt()
    val updatedAt by updatedAt()
    val completed by boolean("completed").default(false).bindTo { it.completed }
}