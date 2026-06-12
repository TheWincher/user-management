package louchez.emmanuel.domain.error

sealed class AuthError(message: String) : Exception(message) {
    class InvalidCredentials : AuthError("Invalid credentials")
    class Unauthorized : AuthError("Unauthorized")
    class Forbidden : AuthError("Forbidden")
}
