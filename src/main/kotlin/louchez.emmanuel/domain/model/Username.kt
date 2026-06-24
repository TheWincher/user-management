package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.core.Result


@ConsistentCopyVisibility
data class Username private constructor(val value: String) {
    companion object {
        fun create(input: String): Result<Username, UserError.InvalidUsername> {
            if (input.isBlank()) return Result.Failure(UserError.InvalidUsername(input))

            return Result.Success(Username(input))
        }

        fun fromDb(value: String): Username = Username(value)
    }
}