package louchez.emmanuel

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import louchez.emmanuel.di.JwtConfig
import louchez.emmanuel.di.initTestDatabase
import louchez.emmanuel.di.testModule
import louchez.emmanuel.interfaces.graphql.AppGraphQLExceptionHandler
import louchez.emmanuel.interfaces.graphql.AuthContextFactory
import louchez.emmanuel.interfaces.graphql.AuthMutation
import louchez.emmanuel.interfaces.graphql.UserMutation
import louchez.emmanuel.interfaces.graphql.UserQuery
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin

fun ApplicationTestBuilder.configureTestApp() {
    application {
        initTestDatabase()
        install(Koin) {
            modules(testModule(JwtConfig("test-secret", "test-issuer", "test-audience")))
        }
        install(GraphQL) {
            schema {
                packages = listOf("louchez.emmanuel")
                queries = listOf(get<UserQuery>())
                mutations = listOf(get<UserMutation>(), get<AuthMutation>())
            }
            engine {
                exceptionHandler = AppGraphQLExceptionHandler()
            }
            server {
                contextFactory = AuthContextFactory()
            }
        }
        routing {
            graphQLPostRoute()
        }
    }
}