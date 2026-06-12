package louchez.emmanuel.interfaces.graphql

import louchez.emmanuel.application.usecase.user.GetUserUseCase
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import louchez.emmanuel.application.usecase.user.GetCurrentUserUseCase

class UserQuery(private val getUserUseCase: GetUserUseCase, private val getCurrentUserUseCase: GetCurrentUserUseCase) :
    Query {
    fun me(dfe: DataFetchingEnvironment): UserResponse {
        val authContext = dfe.graphQlContext.get<AuthContext>(AuthContext::class)
        return getCurrentUserUseCase.execute(authContext.accessToken)
            .getOrElse { throw it }
            .toResponse()
    }

    fun user(id: String): UserResponse {
        return getUserUseCase.execute(id).getOrElse { throw it }.toResponse()
    }
}