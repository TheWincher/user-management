package louchez.emmanuel.domain.model

import louchez.emmanuel.domain.error.UserError
import java.time.LocalDateTime
import java.util.UUID

@ConsistentCopyVisibility
data class User private constructor(
    val id: UserId,
    val email: Email,
    val username: Username,
    val hashedPassword: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun create(email: Email, username: Username, hashedPassword: String): User {
            return User(UserId.generate(), email, username, hashedPassword, LocalDateTime.now())
        }

        fun reconstitute(
            id: UUID,
            email: String,
            username: String,
            hashedPassword: String,
            createdAt: LocalDateTime
        ): User {
            return User(UserId.fromDb(id), Email.fromDb(email), Username.fromDb(username), hashedPassword, createdAt)
        }
    }
}