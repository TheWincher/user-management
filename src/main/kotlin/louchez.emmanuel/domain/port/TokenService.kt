package louchez.emmanuel.domain.port

import louchez.emmanuel.domain.model.AccessToken
import louchez.emmanuel.domain.model.RefreshToken
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId

interface TokenService {
    fun generateAccessToken(user: User): AccessToken
    fun generateRefreshToken(user: User): RefreshToken
    fun verifyAccessToken(token: String): UserId?
    fun verifyRefreshToken(token: String): UserId?
}