package louchez.emmanuel.application.usecase.auth

import louchez.emmanuel.domain.error.AuthError
import louchez.emmanuel.domain.model.AccessToken
import louchez.emmanuel.domain.model.Email
import louchez.emmanuel.domain.model.Password
import louchez.emmanuel.domain.model.RefreshToken
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.port.PasswordHasher
import louchez.emmanuel.domain.port.TokenService
import louchez.emmanuel.domain.port.UserRepository

data class LoginResult(val user: User, val accessToken: AccessToken, val refreshToken: RefreshToken)

class LoginUseCase(
    val userRepository: UserRepository,
    val passwordHasher: PasswordHasher,
    val tokenService: TokenService
) {
    fun execute(rawEmail: String, rawPassword: String): Result<LoginResult> {
        val email = Email.create(rawEmail).getOrElse { return Result.failure(AuthError.InvalidCredentials()) }
        val user = userRepository.findByEmail(email) ?: return Result.failure(AuthError.InvalidCredentials())

        val password = Password.create(rawPassword).getOrElse { return Result.failure(AuthError.InvalidCredentials()) }
        if (!passwordHasher.verify(password, user.hashedPassword)) {
            return Result.failure(AuthError.InvalidCredentials())
        }

        val accessToken = tokenService.generateAccessToken(user)
        val refreshToken = tokenService.generateRefreshToken(user)

        return Result.success(LoginResult(user, accessToken, refreshToken))
    }
}