package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.AppError
import louchez.emmanuel.domain.error.UserError

sealed interface GetUserError : AppError {
    data class InvalidUserId(val error: UserError.InvalidUserId) : GetUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }

    data class UserNotFound(val error: UserError.UserNotFound) : GetUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }
}