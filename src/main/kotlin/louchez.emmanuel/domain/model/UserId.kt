package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import java.util.UUID

@ConsistentCopyVisibility
data class UserId private constructor(val value: UUID) {
    companion object {
        fun generate(): UserId {
            return UserId(UUID.randomUUID())
        }

        fun from(id: String): Result<UserId> {
            return try {
                Result.success(UserId(UUID.fromString(id)))
            } catch (e: IllegalArgumentException) {
                Result.failure(UserError.InvalidUserId(id))
            }
        }

        fun fromDb(value: UUID): UserId = UserId(value)
    }
}