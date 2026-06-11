package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError


@ConsistentCopyVisibility
data class Username private constructor(val value: String) {
    companion object {
        fun create(input: String): Result<Username> {
            if (input.isBlank()) return Result.failure(UserError.InvalidUsername(input))

            return Result.success(Username(input))
        }

        fun fromDb(value: String): Username = Username(value)
    }
}