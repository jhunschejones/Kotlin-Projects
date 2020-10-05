package cats

import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.catRouter(catService: CatService) {
    route("/cats") {
        post {
            val params = context.receiveParameters()
            val name = requireNotNull(params["name"])
            val age = params["age"]?.toInt()
            val newCatId = catService.create(name, age)
            context.response.status(HttpStatusCode.Created)
            context.respond(mapOf( "id" to  newCatId, "name" to name, "age" to age))
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
