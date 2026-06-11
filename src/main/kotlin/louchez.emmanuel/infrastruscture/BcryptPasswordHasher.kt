package louchez.emmanuel.infrastruscture

import louchez.emmanuel.domain.model.Password
import louchez.emmanuel.domain.port.PasswordHasher
import org.mindrot.jbcrypt.BCrypt

class BcryptPasswordHasher: PasswordHasher {
    override fun hash(rawPassword: Password): String {
        return BCrypt.hashpw(rawPassword.value, BCrypt.gensalt())
    }

    override fun verify(
        rawPassword: Password,
        hashedPassword: String
    ): Boolean {
        return BCrypt.checkpw(rawPassword.value, hashedPassword)
    }
}