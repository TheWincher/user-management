package louchez.emmanuel.application.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import louchez.emmanuel.application.usecase.user.GetCurrentUserError
import louchez.emmanuel.application.usecase.user.GetCurrentUserUseCase
import louchez.emmanuel.application.usecase.user.GetUserUseCase
import louchez.emmanuel.assertFailure
import louchez.emmanuel.assertSuccess
import louchez.emmanuel.domain.error.AuthError
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId
import louchez.emmanuel.domain.port.TokenService
import louchez.emmanuel.domain.port.UserRepository
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCurrentUserUseCaseTest {
    private val tokenService = mockk<TokenService>()
    private val userRepository = mockk<UserRepository>()
    private val getUserUseCase = GetUserUseCase(userRepository)
    private val useCase = GetCurrentUserUseCase(tokenService, getUserUseCase)

    @Test
    fun `should get current user successfully`() {
        val userId = UserId.generate()
        val createdAt = LocalDateTime.now()

        every { tokenService.verifyAccessToken(any()) } returns userId

        every { userRepository.findById(any()) } returns User.reconstitute(
            userId.value,
            "test@example.com",
            "john",
            "hashed_value",
            createdAt
        )

        val result = useCase.execute("access_token")

        assertTrue(result.isSuccess)
        assertEquals(userId, result.assertSuccess().id)
        assertEquals(createdAt, result.assertSuccess().createdAt)
        assertEquals("john", result.assertSuccess().username.value)
        assertEquals("test@example.com", result.assertSuccess().email.value)
        assertEquals("hashed_value", result.assertSuccess().hashedPassword)

        verify { tokenService.verifyAccessToken(any()) }
        verify { userRepository.findById(any()) }
    }

    @Test
    fun `should fail when no access token`() {

        val result = useCase.execute(null)
        assertTrue(result.isFailure)
        assertTrue(result.assertFailure() is GetCurrentUserError.Unauthorized)

        verify(exactly = 0) { tokenService.verifyAccessToken(any()) }
        verify(exactly = 0) { userRepository.findById(any()) }
    }

    @Test
    fun `should fail when bad token`() {
        every { tokenService.verifyAccessToken(any()) } returns null

        val result = useCase.execute("bad_access_token")
        assertTrue(result.isFailure)
        assertTrue(result.assertFailure() is GetCurrentUserError.Unauthorized)

        verify { tokenService.verifyAccessToken(any()) }
        verify(exactly = 0) { userRepository.findById(any()) }
    }
}