package louchez.emmanuel.domain.error

import java.util.UUID

sealed class UserError(message: String): Exception(message) {
    data class UserNotFound(val id: UUID) : UserError("User $id Not Found")
    data class EmailAlreadyUsed(val email: String) : UserError("Email $email already used")
    data class InvalidEmail(val email: String) : UserError("Email $email is invalid")
    data class InvalidUsername(val username: String) : UserError("Username $username is invalid")
    class InvalidPassword : UserError("Password is invalid")
    data class InvalidUserId(val id: String): UserError("UserId $id is invalid")
}

