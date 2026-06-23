package louchez.emmanuel.di

import louchez.emmanuel.application.usecase.auth.LoginUseCase
import louchez.emmanuel.application.usecase.user.GetCurrentUserUseCase
import louchez.emmanuel.application.usecase.user.GetUserUseCase
import louchez.emmanuel.application.usecase.user.RegisterUserUseCase
import louchez.emmanuel.domain.port.PasswordHasher
import louchez.emmanuel.domain.port.TokenService
import louchez.emmanuel.domain.port.UserRepository
import louchez.emmanuel.infrastruscture.BcryptPasswordHasher
import louchez.emmanuel.infrastruscture.auth.JwtTokenService
import louchez.emmanuel.infrastruscture.repository.UserRepositoryPostgresImpl
import louchez.emmanuel.interfaces.graphql.AuthMutation
import louchez.emmanuel.interfaces.graphql.UserMutation
import louchez.emmanuel.interfaces.graphql.UserQuery
import org.koin.dsl.module

fun testModule(jwtConfig: JwtConfig) = module {
    single<UserRepository> { UserRepositoryPostgresImpl() }  // même implémentation !
    single<PasswordHasher> { BcryptPasswordHasher() }
    single { jwtConfig }
    single<TokenService> { JwtTokenService(jwtConfig.secret, jwtConfig.issuer, jwtConfig.audience) }
    single { RegisterUserUseCase(get(), get()) }
    single { GetUserUseCase(get()) }
    single { GetCurrentUserUseCase(get(), get()) }
    single { LoginUseCase(get(), get(), get()) }
    single { UserQuery(get(), get()) }
    single { UserMutation(get()) }
    single { AuthMutation(get(), get(), get()) }
}
