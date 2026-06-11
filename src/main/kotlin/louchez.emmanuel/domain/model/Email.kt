package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError

@ConsistentCopyVisibility
data class Email private constructor(val value: String) {
    companion object {
        fun create(input: String): Result<Email> {
            val normalizedInput = input.lowercase().trim()
            val emailRegex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
            if (!emailRegex.matches(normalizedInput)) {
                return Result.failure(UserError.InvalidEmail(normalizedInput))
            }

            return Result.success(Email(normalizedInput))
        }

        fun fromDb(value: String): Email = Email(value)
    }
}