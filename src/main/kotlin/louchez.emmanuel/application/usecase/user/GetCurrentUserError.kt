package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.AppError
import louchez.emmanuel.domain.error.AuthError

sealed interface GetCurrentUserError : AppError {
    data class Unauthorized(val error: AuthError.Unauthorized) : GetCurrentUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }

    data class FromGetUser(val error: GetUserError) : GetCurrentUserError {
        override val code = error.code
        override val classification = error.classification
        override val message = error.message
    }
}