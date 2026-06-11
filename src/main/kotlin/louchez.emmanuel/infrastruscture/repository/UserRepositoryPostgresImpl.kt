package louchez.emmanuel.infrastruscture.repository

import louchez.emmanuel.domain.model.Email
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId
import louchez.emmanuel.domain.port.UserRepository
import louchez.emmanuel.infrastruscture.database.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

class UserRepositoryPostgresImpl : UserRepository {
    override fun findById(id: UserId): User? {
        return transaction {
            UserTable.selectAll().where { UserTable.id eq id.value }.singleOrNull()
                ?.let { row -> toUser(row) }
        }
    }

    override fun findByEmail(email: Email): User? {
        return transaction {
            UserTable.selectAll().where { UserTable.email eq email.value }.singleOrNull()
                ?.let { row -> toUser(row) }
        }
    }

    override fun save(user: User): User {
        transaction {
            UserTable.upsert {
                it[id] = user.id.value
                it[email] = user.email.value
                it[username] = user.username.value
                it[hashedPassword] = user.hashedPassword
                it[createdAt] = user.createdAt
            }
        }

        return user
    }

    override fun delete(id: UserId): Boolean {
        return transaction {
            UserTable.deleteWhere { UserTable.id eq id.value }
        } > 0
    }

    private fun toUser(row: ResultRow): User {
        return User.reconstitute(
            row[UserTable.id].value,
            row[UserTable.email],
            row[UserTable.username],
            row[UserTable.hashedPassword],
            row[UserTable.createdAt]
        )
    }
}
