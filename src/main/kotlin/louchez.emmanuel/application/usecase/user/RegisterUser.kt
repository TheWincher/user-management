package louchez.emmanuel.application.usecase.user

import louchez.emmanuel.domain.error.UserError
import louchez.emmanuel.domain.core.Result
import louchez.emmanuel.domain.model.Email
import louchez.emmanuel.domain.model.Password
import louchez.emmanuel.domain.model.User
import louchez.emmanuel.domain.model.Username
import louchez.emmanuel.domain.port.PasswordHasher
import louchez.emmanuel.domain.port.UserRepository

class RegisterUserUseCase(val userRepository: UserRepository, val passwordHasher: PasswordHasher) {
    fun execute(rawEmail: String, rawUsername: String, rawPassword: String): Result<User, RegisterUserError> {
        val password =
            Password.create(rawPassword).getOrElse { return Result.Failure(RegisterUserError.InvalidPassword(it)) }

        val email = Email.create(rawEmail).getOrElse { return Result.Failure(RegisterUserError.InvalidEmail(it)) }

        val username =
            Username.create(rawUsername).getOrElse { return Result.Failure(RegisterUserError.InvalidUsername(it)) }

        if (userRepository.findByEmail(email) != null) {
            return Result.Failure(RegisterUserError.EmailAlreadyUsed(UserError.EmailAlreadyUsed(email.value)))
        }

        val hashedPassword = passwordHasher.hash(password)
        val user = userRepository.save(User.create(email, username, hashedPassword))

        return Result.Success(user)
    }
}