package louchez.emmanuel.domain.error

import java.util.UUID

sealed class UserError(message: String, override val code: String, override val classification: ErrorClassification) :
    Exception(message), AppError {
    data class UserNotFound(val id: UUID) :
        UserError("User $id Not Found", "USER_NOT_FOUND", ErrorClassification.NOT_FOUND)

    data class EmailAlreadyUsed(val email: String) :
        UserError("Email $email already used", "EMAIL_ALREADY_USED", ErrorClassification.CONFLICT)

    data class InvalidEmail(val email: String) :
        UserError("Email $email is invalid", "INVALID_EMAIL", ErrorClassification.VALIDATION_ERROR)

    data class InvalidUsername(val username: String) :
        UserError("Username $username is invalid", "INVALID_USERNAME", ErrorClassification.VALIDATION_ERROR)

    class InvalidPassword : UserError("Password is invalid", "INVALID_PASSWORD", ErrorClassification.VALIDATION_ERROR)
    data class InvalidUserId(val id: String) :
        UserError("UserId $id is invalid", "INVALID_USER_ID", ErrorClassification.VALIDATION_ERROR)
}

