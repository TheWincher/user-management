package louchez.emmanuel.interfaces.graphql

import louchez.emmanuel.application.usecase.user.GetUserUseCase
import com.expediagroup.graphql.server.operations.Query

class UserQuery(private val getUserUseCase: GetUserUseCase) : Query {
    fun user(id: String): UserResponse {
        return getUserUseCase.execute(id).getOrElse { throw it }.toResponse()
    }
}