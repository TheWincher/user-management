package louchez.emmanuel.interfaces.graphql

import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import io.ktor.http.Cookie
import io.ktor.util.date.GMTDate
import louchez.emmanuel.application.usecase.auth.LoginUseCase
import louchez.emmanuel.domain.port.TokenService
import louchez.emmanuel.domain.port.UserRepository

class AuthMutation(
    private val loginUseCase: LoginUseCase,
    private val tokenService: TokenService,
    private val userRepository: UserRepository
) : Mutation {
    fun login(email: String, password: String, dfe: DataFetchingEnvironment): UserResponse {
        val authContext = dfe.graphQlContext.get<AuthContext>(AuthContext::class)
        val loginResult = loginUseCase.execute(email, password).getOrElse { throw it }

        authContext.call.response.cookies.append(
            Cookie(
                name = "access_token",
                value = loginResult.accessToken.value,
                httpOnly = true,
                path = "/"
            )
        )

        authContext.call.response.cookies.append(
            Cookie(
                name = "refresh_token",
                value = loginResult.refreshToken.value,
                httpOnly = true,
                path = "/"
            )
        )

        return loginResult.user.toResponse()
    }

    fun refresh(dfe: DataFetchingEnvironment): Boolean {
        val authContext = dfe.graphQlContext.get<AuthContext>(AuthContext::class)
        val refreshToken = authContext.refreshToken ?: return false

        val userId = tokenService.verifyRefreshToken(refreshToken) ?: return false
        val user = userRepository.findById(userId) ?: return false

        val accessToken = tokenService.generateAccessToken(user)
        authContext.call.response.cookies.append(
            Cookie(
                name = "access_token",
                value = accessToken.value,
                httpOnly = true,
                path = "/"
            )
        )

        return true
    }

    fun logout(dfe: DataFetchingEnvironment): Boolean {
        val authContext = dfe.graphQlContext.get<AuthContext>(AuthContext::class)

        authContext.call.response.cookies.append(
            Cookie(
                name = "access_token",
                value = "",
                httpOnly = true,
                path = "/",
                expires = GMTDate.START
            )
        )

        authContext.call.response.cookies.append(
            Cookie(
                name = "refresh_token",
                value = "",
                httpOnly = true,
                path = "/",
                expires = GMTDate.START
            )
        )

        return true
    }
}
