package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import kotlin.test.*

class PasswordTest {

    @Test
    fun `should create valid password`() {
        val result = Password.create("Secure#123")
        assertTrue(result.isSuccess)
        assertEquals("Secure#123", result.getOrThrow().value)
    }

    @Test
    fun `should fail when password too short`() {
        val result = Password.create("Ab1#")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is UserError.InvalidPassword)
    }

    @Test
    fun `should fail when no uppercase`() {
        val result = Password.create("secure#123")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is UserError.InvalidPassword)
    }

    @Test
    fun `should fail when no digit`() {
        val result = Password.create("Secure#abc")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is UserError.InvalidPassword)
    }

    @Test
    fun `should fail when no special char`() {
        val result = Password.create("Secure123")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is UserError.InvalidPassword)
    }
}
