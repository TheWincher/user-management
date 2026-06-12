package louchez.emmanuel.infrastruscture.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import louchez.emmanuel.domain.model.AccessToken
import louchez.emmanuel.domain.model.RefreshToken
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId
import louchez.emmanuel.domain.port.TokenService
import java.util.Date
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant
import kotlin.time.toJavaInstant

class JwtTokenService(val secret: String, val issuer: String, val audience: String) : TokenService {
    override fun generateAccessToken(user: User): AccessToken {
        val expiredAt = Clock.System.now().plus(15.minutes)
        val jwt = JWT.create()
            .withSubject(user.id.value.toString())
            .withExpiresAt(Date.from(expiredAt.toJavaInstant()))
            .withIssuer(issuer)
            .withAudience(audience)
            .sign(Algorithm.HMAC256(secret))

        return AccessToken(jwt, expiredAt)
    }

    override fun generateRefreshToken(user: User): RefreshToken {
        val expiredAt = Clock.System.now().plus(7.days)
        val jwt = JWT.create()
            .withSubject(user.id.value.toString())
            .withExpiresAt(Date.from(expiredAt.toJavaInstant()))
            .withIssuer(issuer)
            .withAudience(audience)
            .sign(Algorithm.HMAC256(secret))

        return RefreshToken(jwt, expiredAt)
    }

    override fun verifyAccessToken(token: String) = verifyToken(token)
    override fun verifyRefreshToken(token: String) = verifyToken(token)

    private fun verifyToken(token: String): UserId? {
        return try {
            val jwt = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .withAudience(audience)
                .build()
                .verify(token)
            val subject = jwt.subject ?: return null
            UserId.from(subject).getOrNull()
        } catch (e: Exception) {
            null
        }
    }
}