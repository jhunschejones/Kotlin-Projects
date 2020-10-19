import cats.Cats
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CatsTest {
    @BeforeEach
    fun cleanup() {
        DB.connect()
        transaction {
            SchemaUtils.drop(Cats)
        }
    }

    @Test
    fun `#show returns message with cat name`() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "/cats/kirito")
            assertEquals(
                """{ "name" : "Kirito", "message" : "Kirito is not staying in our hostel yet." }""".asJson(),
                call.response.content?.asJson()
            )
        }
    }

    @Test
    fun `#create adds a cat to the database`() {
        withTestApplication(Application::mainModule) {
            val call = createCat("Kirito", 3)
            assertEquals(HttpStatusCode.Created, call.response.status())
            assertEquals("""{"id":1,"name":"Kirito","age":3}""".asJson(), call.response.content?.asJson())
        }
    }

    @Test
    fun `#create raises on duplicate name`() {
        withTestApplication(Application::mainModule) {
            createCat("Kirito", 3)

            val call = createCat("Kirito", 5)
            assertEquals(HttpStatusCode.BadRequest, call.response.status())
            assertEquals(
                """{"error":"Detail: Key (name)=(Kirito) already exists."}""".asJson(),
                call.response.content?.asJson()
            )
        }
    }

    @Test
    fun `index returns all cats`() {
        withTestApplication(Application::mainModule) {
            val initial = handleRequest(HttpMethod.Get, "/cats")
            createCat("Kirito", 3)
            val afterCreate = handleRequest(HttpMethod.Get, "/cats")

            assertEquals("[]".asJson(), initial.response.content?.asJson())
            assertEquals("""[{"id":1,"name":"Kirito","age":3}]""".asJson(), afterCreate.response.content?.asJson())
        }
    }
}

private fun String.asJson() = ObjectMapper().readTree(this)

fun TestApplicationEngine.createCat(name: String, age: Int) = handleRequest(HttpMethod.Post, "/cats") {
    addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
    setBody(listOf("name" to name, "age" to age.toString()).formUrlEncode())
}
