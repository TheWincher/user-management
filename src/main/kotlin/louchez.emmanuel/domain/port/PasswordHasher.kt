package louchez.emmanuel.domain.port

import louchez.emmanuel.domain.model.Password

interface PasswordHasher {
    fun hash(rawPassword: Password): String
    fun verify(rawPassword: Password, hashedPassword: String): Boolean
}