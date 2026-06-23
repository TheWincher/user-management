package louchez.emmanuel.interfaces.graphql

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import louchez.emmanuel.configureTestApp
import louchez.emmanuel.infrastruscture.database.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthMutationTest {

    @AfterEach
    fun cleanup() {
        transaction {
            SchemaUtils.drop(UserTable)
        }
    }

    @Test
    fun `should register and login successfully`() = testApplication {
        configureTestApp()

        val response = client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody(
                """
        {"query": "mutation { registerUser(email: \"test@example.com\", username: \"john\", password: \"Secure#123\") { id email username } }"}
    """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("test@example.com"))
        assertTrue(body.contains("john"))
    }

    @Test
    fun `should login after registration`() = testApplication {
        configureTestApp()

        client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody("""{"query": "mutation { registerUser(email: \"test@example.com\", username: \"john\", password: \"Secure#123\") { id email username } }"}""")
        }

        val loginResponse = client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody("""{"query": "mutation { login(email: \"test@example.com\", password: \"Secure#123\") { id } }"}""")
        }

        assertEquals(HttpStatusCode.OK, loginResponse.status)
        assertEquals(loginResponse.headers.getAll("Set-Cookie")?.any { it.contains("access_token") }, true)
    }

    @Test
    fun `should fail login with wrong password`() = testApplication {
        configureTestApp()

        client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody("""{"query": "mutation { registerUser(email: \"test@example.com\", username: \"john\", password: \"Secure#123\") { id } }"}""")
        }

        val response = client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody("""{"query": "mutation { login(email: \"test@example.com\", password: \"WrongPass#1\") { id } }"}""")
        }

        val body = response.bodyAsText()
        assertTrue(body.contains("INVALID_CREDENTIALS"))
    }
}