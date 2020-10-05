import cats.CatService
import cats.CatServiceDB
import cats.catRouter
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val port = 8080
    val server = embeddedServer(Netty, port, module = Application::mainModule)
    server.start()
}

fun Application.mainModule() {
    setUpDatabase()
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    routing {
        trace {
            application.log.info(it.buildText())
        }
        get {
            context.respond(mapOf("message" to "Welcome to the cat hostel."))
        }
        catRouter(CatServiceDB())
    }
}

private fun setUpDatabase() {
    val dbHost = "localhost"
    val dbPort = 5432
    val dbName = "cats_hostel_development"
    val dbUser = "cats_hostel_development_user"
    val dbPassword = "very_secret"
    val db = Database.connect(
        "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )

    transaction {
        SchemaUtils.create(Cats)
    }
}
