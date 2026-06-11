import louchez.emmanuel.application.usecase.user.GetUserUseCase
import louchez.emmanuel.application.usecase.user.RegisterUserUseCase
import louchez.emmanuel.domain.port.PasswordHasher
import louchez.emmanuel.domain.port.UserRepository
import louchez.emmanuel.infrastruscture.BcryptPasswordHasher
import louchez.emmanuel.infrastruscture.repository.UserRepositoryPostgresImpl
import louchez.emmanuel.interfaces.graphql.UserMutation
import louchez.emmanuel.interfaces.graphql.UserQuery
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryPostgresImpl() }
    single<PasswordHasher> { BcryptPasswordHasher() }
    single { RegisterUserUseCase(get(), get()) }
    single { GetUserUseCase(get()) }
    single { UserQuery(get()) }
    single { UserMutation(get()) }
}