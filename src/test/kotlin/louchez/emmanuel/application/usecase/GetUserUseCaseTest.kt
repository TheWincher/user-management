package louchez.emmanuel.application.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import louchez.emmanuel.application.usecase.user.GetUserError
import louchez.emmanuel.application.usecase.user.GetUserUseCase
import louchez.emmanuel.assertFailure
import louchez.emmanuel.assertSuccess
import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId
import louchez.emmanuel.domain.port.UserRepository
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.*

class GetUserUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val useCase = GetUserUseCase(userRepository)

    @Test
    fun `should get user successfully`() {
        val userId = UserId.generate()
        val createdAt = LocalDateTime.now()

        every { userRepository.findById(any()) } returns User.reconstitute(
            userId.value,
            "test@example.com",
            "john",
            "hashed_value",
            createdAt
        )

        val result = useCase.execute(userId.value.toString())

        assertTrue(result.isSuccess)
        assertEquals(userId, result.assertSuccess().id)
        assertEquals(createdAt, result.assertSuccess().createdAt)
        assertEquals("john", result.assertSuccess().username.value)
        assertEquals("test@example.com", result.assertSuccess().email.value)
        assertEquals("hashed_value", result.assertSuccess().hashedPassword)

        verify { userRepository.findById(any()) }
    }

    @Test
    fun `should fail when id is not uuid`() {
        val result = useCase.execute("test")

        assertTrue(result.isFailure)
        assertTrue(result.assertFailure() is GetUserError.InvalidUserId)
        verify(exactly = 0) { userRepository.findById(any()) }
    }

    @Test
    fun `should fail when user not found`() {
        every { userRepository.findById(any()) } returns null

        val userId = UserId.generate()
        val result = useCase.execute(userId.value.toString())

        assertTrue(result.isFailure)
        assertTrue(result.assertFailure() is GetUserError.UserNotFound)
        verify { userRepository.findById(any()) }
    }
}