package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.core.Result
import java.util.UUID

@ConsistentCopyVisibility
data class UserId private constructor(val value: UUID) {
    companion object {
        fun generate(): UserId {
            return UserId(UUID.randomUUID())
        }

        fun from(id: String): Result<UserId, UserError.InvalidUserId> {
            return try {
                Result.Success(UserId(UUID.fromString(id)))
            } catch (e: IllegalArgumentException) {
                Result.Failure(UserError.InvalidUserId(id))
            }
        }

        fun fromDb(value: UUID): UserId = UserId(value)
    }
}