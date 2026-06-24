package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.core.Result

@ConsistentCopyVisibility
data class Password private constructor(val value: String) {
    companion object {
        fun create(value: String): Result<Password, UserError.InvalidPassword> {
            val passwordRegex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
            if (value.matches(passwordRegex)) return Result.Success(Password(value))
            return Result.Failure(UserError.InvalidPassword())
        }
    }
}