package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.core.Result

@ConsistentCopyVisibility
data class Email private constructor(val value: String) {
    companion object {
        fun create(input: String): Result<Email, UserError.InvalidEmail> {
            val normalizedInput = input.lowercase().trim()
            val emailRegex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
            if (!emailRegex.matches(normalizedInput)) {
                return Result.Failure(UserError.InvalidEmail(normalizedInput))
            }

            return Result.Success(Email(normalizedInput))
        }

        fun fromDb(value: String): Email = Email(value)
    }
}