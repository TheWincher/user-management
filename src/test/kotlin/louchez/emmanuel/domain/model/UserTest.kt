package louchez.emmanuel.domain.model

import louchez.emmanuel.assertSuccess
import louchez.emmanuel.domain.error.UserError
import kotlin.test.*

class UserTest {

    @Test
    fun `should create user`() {
        val email = Email.create("test@example.com").assertSuccess()
        val username = Username.create("test").assertSuccess()

        val user = User.create(email, username, "hashedPassword")

        assertEquals("test@example.com", user.email.value)
        assertEquals("hashedPassword", user.hashedPassword)
        assertEquals("test", user.username.value)
    }

    @Test
    fun `should generate id and createdAt on creation`() {
        val email = Email.create("test@example.com").assertSuccess()
        val username = Username.create("test").assertSuccess()

        val user1 = User.create(email, username, "hash")
        val user2 = User.create(email, username, "hash")

        assertNotEquals(user1.id, user2.id)
    }
}
