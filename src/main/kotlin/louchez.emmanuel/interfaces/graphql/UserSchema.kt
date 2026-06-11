package louchez.emmanuel.interfaces.graphql

import com.apurebase.kgraphql.GraphQL
import io.ktor.server.application.Application
import io.ktor.server.application.install
import louchez.emmanuel.application.usecase.user.GetUserUseCase
import louchez.emmanuel.application.usecase.user.RegisterUserUseCase
import louchez.emmanuel.domain.model.User
import org.koin.ktor.ext.getKoin

data class UserResponse(
    val id: String,
    val email: String,
    val username: String,
    val createdAt: String
)

fun User.toResponse() = UserResponse(
    id = id.value.toString(),
    email = email.value,
    username = username.value,
    createdAt = createdAt.toString()
)

fun Application.configureGraphQL() {
    val getUserUseCase = getKoin().get<GetUserUseCase>()
    val registerUserUseCase = getKoin().get<RegisterUserUseCase>()

    install(GraphQL) {
        playground = true
        schema {
            type<UserResponse>()

            query("user") {
                resolver { id: String ->
                    val user = getUserUseCase.execute(id).getOrElse { throw it }
                    user.toResponse()
                }
            }

            mutation("registerUser") {
                resolver { email: String, username: String, password: String ->
                    val user =
                        registerUserUseCase.execute(email, username, password).getOrElse { throw it }
                    user.toResponse()
                }
            }
        }
    }
}