import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CatsTest {
    @Test
    fun `cats returns message with cat name`() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "/cats/kirito")
            assertEquals(
                """{ "name" : "Kirito", "message" : "Kirito is not staying in our hostel yet." }""".asJson(),
                call.response.content?.asJson()
            )
        }
    }

    @Test
    fun `create adds a cat to the database`() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Post, "/cats") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("name" to "Kirito", "age" to "3").formUrlEncode())
            }
            assertEquals(HttpStatusCode.Created, call.response.status())
            assertTrue { call.response.content!!.contains("Kirito") }
        }
    }
}

private fun String.asJson() = ObjectMapper().readTree(this)
