package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError

@ConsistentCopyVisibility
data class Password private constructor(val value: String) {
    companion object {
        fun create(value: String): Result<Password> {
            val passwordRegex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
            if (value.matches(passwordRegex)) return Result.success(Password(value))
            return Result.failure(UserError.InvalidPassword())
        }
    }
}