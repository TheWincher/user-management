package louchez.emmanuel.interfaces.graphql

import louchez.emmanuel.domain.model.User

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