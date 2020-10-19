package cats

import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException

fun Route.catRouter(catService: CatService) {
    route("/cats") {
        post {
            val params = context.receiveParameters()
            val name = requireNotNull(params["name"])
            val age = params["age"]?.toInt()
            try {
                val newCatId = catService.create(name, age)
                context.response.status(HttpStatusCode.Created)
                context.respond(mapOf( "id" to  newCatId, "name" to name, "age" to age))
            } catch(e: ExposedSQLException) {

                val sanitizedError = Regex("Detail.*").find(e.localizedMessage)?.value
                context.respond(HttpStatusCode.BadRequest, mapOf("error" to sanitizedError))
            }
        }
        get {
            context.respond(catService.all())
        }
        get("/{name}") {
            val name = context.parameters["name"]?.capitalize()
            context.respond(
                mapOf(
                    "name" to name,
                    "message" to "$name is not staying in our hostel yet."
                )
            )
        }
    }
}
