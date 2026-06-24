package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.UserId
import louchez.emmanuel.domain.port.UserRepository
import louchez.emmanuel.domain.core.Result

class GetUserUseCase(val userRepository: UserRepository) {
    fun execute(id: String): Result<User, GetUserError> {
        val userId = UserId.from(id).getOrElse { return Result.Failure(GetUserError.InvalidUserId(it)) }
        val user = userRepository.findById(userId) ?: return Result.Failure(
            GetUserError.UserNotFound(
                UserError.UserNotFound(userId.value)
            )
        )

        return Result.Success(user)
    }
}