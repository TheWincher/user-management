package louchez.emmanuel.domain.error

sealed class AuthError(
    override val message: String,
    override val code: String,
    override val classification: ErrorClassification
) :
    AppError {
    class InvalidCredentials : AuthError("Invalid credentials", "INVALID_CREDENTIALS", ErrorClassification.UNAUTHORIZED)
    class Unauthorized : AuthError("Unauthorized", "UNAUTHORIZED", ErrorClassification.UNAUTHORIZED)
    class Forbidden : AuthError("Forbidden", "FORBIDDEN", ErrorClassification.FORBIDDEN)
}
