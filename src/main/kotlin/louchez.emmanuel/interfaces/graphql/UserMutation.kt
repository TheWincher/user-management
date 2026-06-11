package louchez.emmanuel.interfaces.graphql

import louchez.emmanuel.application.usecase.user.RegisterUserUseCase
import com.expediagroup.graphql.server.operations.Mutation

class UserMutation(private val registerUserUseCase: RegisterUserUseCase) : Mutation {
    fun registerUser(email: String, username: String, password: String): UserResponse {
        return registerUserUseCase.execute(email, username, password).getOrElse { throw it }.toResponse()
    }
}
