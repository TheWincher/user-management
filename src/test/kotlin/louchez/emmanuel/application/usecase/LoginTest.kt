package louchez.emmanuel.application.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import louchez.emmanuel.application.usecase.auth.LoginUseCase
import louchez.emmanuel.assertFailure
import louchez.emmanuel.assertSuccess
import louchez.emmanuel.domain.error.AuthError
import louchez.emmanuel.domain.model.AccessToken
import louchez.emmanuel.domain.model.RefreshToken
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId
import louchez.emmanuel.domain.port.PasswordHasher
import louchez.emmanuel.domain.port.TokenService
import louchez.emmanuel.domain.port.UserRepository
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

class LoginTest {
    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val tokenService = mockk<TokenService>()
    private val useCase = LoginUseCase(userRepository, passwordHasher, tokenService)

    @Test
    fun `should login successfully`() {
        val userId = UserId.generate()
        val createdAt = LocalDateTime.now()

        every { userRepository.findByEmail(any()) } returns User.reconstitute(
            userId.value,
            "test@example.com",
            "john",
            "hashed_value",
            createdAt
        )

        every { passwordHasher.verify(any(), any()) } returns true

        val now = Clock.System.now()
        every { tokenService.generateAccessToken(any()) } returns AccessToken(
            "access_token",
            now.plus(15.minutes)
        )

        every { tokenService.generateRefreshToken(any()) } returns RefreshToken(
            "refresh_token",
            now.plus(7.days)
        )

        val result = useCase.execute("test@example.com", "Secure#123")

        assertTrue(result.isSuccess)
        assertEquals("access_token", result.assertSuccess().accessToken.value)
        assertEquals(now.plus(15.minutes), result.assertSuccess().accessToken.expiresAt)
        assertEquals("refresh_token", result.assertSuccess().refreshToken.value)
        assertEquals(now.plus(7.days), result.assertSuccess().refreshToken.expiresAt)

        verify { userRepository.findByEmail(any()) }
        verify { passwordHasher.verify(any(), any()) }
        verify { tokenService.generateAccessToken(any()) }
        verify { tokenService.generateRefreshToken(any()) }
    }

    @Test
    fun `should fail when user not found`() {
        every { userRepository.findByEmail(any()) } returns null

        val result = useCase.execute("test@example.com", "Secure#123")

        assertTrue(result.isFailure)

        verify { userRepository.findByEmail(any()) }
        verify(exactly = 0) { passwordHasher.verify(any(), any()) }
        verify(exactly = 0) { tokenService.generateAccessToken(any()) }
        verify(exactly = 0) { tokenService.generateRefreshToken(any()) }
    }

    @Test
    fun `should fail when password not matching`() {
        val userId = UserId.generate()
        val createdAt = LocalDateTime.now()

        every { userRepository.findByEmail(any()) } returns User.reconstitute(
            userId.value,
            "test@example.com",
            "john",
            "hashed_value",
            createdAt
        )

        every { passwordHasher.verify(any(), any()) } returns false

        val result = useCase.execute("test@example.com", "Secure#123")

        assertTrue(result.isFailure)

        verify { userRepository.findByEmail(any()) }
        verify { passwordHasher.verify(any(), any()) }
        verify(exactly = 0) { tokenService.generateAccessToken(any()) }
        verify(exactly = 0) { tokenService.generateRefreshToken(any()) }
    }
}