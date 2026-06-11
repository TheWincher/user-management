package louchez.emmanuel.domain.port

import louchez.emmanuel.domain.model.Email
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId

interface UserRepository {
    fun findById(id: UserId): User?
    fun findByEmail(email: Email): User?
    fun save(user: User): User
    fun delete(id: UserId): Boolean
}