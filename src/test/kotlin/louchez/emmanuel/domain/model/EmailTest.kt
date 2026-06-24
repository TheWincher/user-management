package louchez.emmanuel.domain.model

import louchez.emmanuel.assertFailure
import louchez.emmanuel.assertSuccess
import louchez.emmanuel.domain.error.UserError
import kotlin.test.*

class EmailTest {

    @Test
    fun `should create valid email`() {
        val result = Email.create("test@example.com")
        assertTrue(result.isSuccess)
        assertEquals("test@example.com", result.assertSuccess().value)
    }

    @Test
    fun `should normalize email to lowercase`() {
        val result = Email.create("TEST@EXAMPLE.COM")
        assertTrue(result.isSuccess)
        assertEquals("test@example.com", result.assertSuccess().value)
    }

    @Test
    fun `should trim whitespace from email`() {
        val result = Email.create(" test@example.com ")
        assertTrue(result.isSuccess)
        assertEquals("test@example.com", result.assertSuccess().value)
    }

    @Test
    fun `should fail when email has no @`() {
        val result = Email.create("test.exemple.com")
        assertTrue(result.isFailure)
    }

    @Test
    fun `should fail when email has no domain`() {
        val result = Email.create("test@")
        assertTrue(result.isFailure)
    }
}
