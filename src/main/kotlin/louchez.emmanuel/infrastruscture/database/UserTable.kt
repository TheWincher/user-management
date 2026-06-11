package louchez.emmanuel.infrastruscture.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : UUIDTable("users") {
    val email = varchar("email", 255)
    val username = varchar("username", 100)
    val hashedPassword = varchar("hashed_password", 255)
    val createdAt = datetime("created_at")
}