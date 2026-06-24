package louchez.emmanuel.application.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import louchez.emmanuel.application.usecase.user.RegisterUserError
import louchez.emmanuel.application.usecase.user.RegisterUserUseCase
import louchez.emmanuel.assertFailure
import louchez.emmanuel.assertSuccess
import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.port.PasswordHasher
import louchez.emmanuel.domain.port.UserRepository
import kotlin.test.*

class RegisterUserUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val passwordHasher = mockk<PasswordHasher>()
    private val useCase = RegisterUserUseCase(userRepository, passwordHasher)

    @Test
    fun `should register user successfully`() {
        every { userRepository.findByEmail(any()) } returns null
        every { passwordHasher.hash(any()) } returns "hashed_value"
        every { userRepository.save(any()) } answers { firstArg() }

        val result = useCase.execute("test@example.com", "john", "Secure#123")

        assertTrue(result.isSuccess)
        assertEquals("john", result.assertSuccess().username.value)
        assertEquals("test@example.com", result.assertSuccess().email.value)
        assertEquals("hashed_value", result.assertSuccess().hashedPassword)

        verify { userRepository.save(any()) }
    }

    @Test
    fun `should fail when email already used`() {
        every { userRepository.findByEmail(any()) } returns mockk()

        val result = useCase.execute("test@example.com", "john", "Secure#123")

        assertTrue(result.isFailure)
        assertTrue(result.assertFailure() is RegisterUserError.EmailAlreadyUsed)
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should fail when password is invalid`() {
        every { userRepository.findByEmail(any()) } returns null

        val result = useCase.execute("test@example.com", "john", "weak")

        assertTrue(result.isFailure)
        assertTrue(result.assertFailure() is RegisterUserError.InvalidPassword)
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should fail when username is blank`() {
        every { userRepository.findByEmail(any()) } returns null

        val result = useCase.execute("test@example.com", "", "Secure#123")

        assertTrue(result.isFailure)
        assertTrue(result.assertFailure() is RegisterUserError.InvalidUsername)
        verify(exactly = 0) { userRepository.save(any()) }
    }
}