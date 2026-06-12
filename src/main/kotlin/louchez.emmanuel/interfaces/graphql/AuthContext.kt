package louchez.emmanuel.interfaces.graphql

import com.expediagroup.graphql.generator.extensions.plus
import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import com.expediagroup.graphql.server.ktor.KtorGraphQLContextFactory
import graphql.GraphQLContext
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.ApplicationRequest

data class AuthContext(
    val accessToken: String?,
    val refreshToken: String?,
    val call: ApplicationCall
)

class AuthContextFactory : DefaultKtorGraphQLContextFactory() {
    override suspend fun generateContext(request: ApplicationRequest): GraphQLContext =
        super.generateContext(request).plus(
            mapOf(
                AuthContext::class to AuthContext(
                    accessToken = request.cookies["access_token"],
                    refreshToken = request.cookies["refresh_token"],
                    call = request.call
                )
            )
        )
}