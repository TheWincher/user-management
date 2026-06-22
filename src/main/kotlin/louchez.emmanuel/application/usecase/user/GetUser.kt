package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId
import louchez.emmanuel.domain.port.UserRepository

class GetUserUseCase(val userRepository: UserRepository) {
    fun execute(id: String): Result<User> {
        val userId = UserId.from(id).getOrElse { return Result.failure(it) }
        val user = userRepository.findById(userId) ?: return Result.failure(UserError.UserNotFound(userId.value))

        return Result.success(user)
    }
}