package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import kotlin.test.*

class UsernameTest {
    @Test
    fun `should create valid username`() {
        val result = Username.create("test")
        assertTrue(result.isSuccess)
        assertEquals("test", result.getOrThrow().value)
    }

    @Test
    fun `should fail when input is empty`() {
        val result = Username.create("")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is UserError.InvalidUsername)
    }

    @Test
    fun `should fail when input contains only whitespace`() {
        val result = Username.create("   ")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is UserError.InvalidUsername)
    }
}