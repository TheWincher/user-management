package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.AuthError
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.port.TokenService

class GetCurrentUserUseCase(
    private val tokenService: TokenService,
    private val getUserUseCase: GetUserUseCase
) {
    fun execute(accessToken: String?): Result<User> {
        val token = accessToken ?: return Result.failure(AuthError.Unauthorized())
        val userId = tokenService.verifyAccessToken(token)
            ?: return Result.failure(AuthError.Unauthorized())
        return getUserUseCase.execute(userId.value.toString())
    }
}