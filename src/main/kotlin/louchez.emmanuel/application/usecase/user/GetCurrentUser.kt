package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.AuthError
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.port.TokenService
import louchez.emmanuel.domain.core.Result

class GetCurrentUserUseCase(
    private val tokenService: TokenService,
    private val getUserUseCase: GetUserUseCase
) {
    fun execute(accessToken: String?): Result<User, GetCurrentUserError> {
        val token = accessToken ?: return Result.Failure(GetCurrentUserError.Unauthorized(AuthError.Unauthorized()))

        val userId =
            tokenService.verifyAccessToken(token)
                ?: return (Result.Failure(GetCurrentUserError.Unauthorized(AuthError.Unauthorized())))

        return getUserUseCase.execute(userId.value.toString()).mapError { GetCurrentUserError.FromGetUser(it) }
    }
}