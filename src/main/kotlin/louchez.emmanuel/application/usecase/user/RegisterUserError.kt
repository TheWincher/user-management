package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.AppError
import louchez.emmanuel.domain.error.UserError

sealed interface RegisterUserError : AppError {
    data class InvalidEmail(val error: UserError.InvalidEmail) : RegisterUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }

    data class InvalidPassword(val error: UserError.InvalidPassword) : RegisterUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }

    data class InvalidUsername(val error: UserError.InvalidUsername) : RegisterUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }

    data class EmailAlreadyUsed(val error: UserError.EmailAlreadyUsed) : RegisterUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }
}