import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ApplicationTest {

    @Test
    fun `index returns default message`() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "/")
            assertEquals(
                """{ "message" : "Welcome to the cat hostel." } """.asJson(),
                call.response.content?.asJson()
            )
        }
    }
}

private fun String.asJson() = ObjectMapper().readTree(this)
