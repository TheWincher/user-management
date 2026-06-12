package louchez.emmanuel

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing
import louchez.emmanuel.di.JwtConfig
import louchez.emmanuel.di.appModule

import louchez.emmanuel.infrastruscture.database.initDatabase
import louchez.emmanuel.interfaces.graphql.AuthContextFactory
import louchez.emmanuel.interfaces.graphql.AuthMutation
import louchez.emmanuel.interfaces.graphql.UserMutation
import louchez.emmanuel.interfaces.graphql.UserQuery
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.module() {
    val jwtConfig = JwtConfig(
        secret = environment.config.property("jwt.secret").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString()
    )

    install(Koin) {
        slf4jLogger()
        modules(appModule(jwtConfig))
    }

    initDatabase(
        url = environment.config.property("db.url").getString(),
        user = environment.config.property("db.user").getString(),
        dbPassword = environment.config.property("db.password").getString()
    )

    install(GraphQL) {
        schema {
            packages = listOf("louchez.emmanuel")
            queries = listOf(UserQuery(get(), get()))
            mutations = listOf(UserMutation(get()), AuthMutation(get(), get(), get()))
        }

        server {
            contextFactory = AuthContextFactory()
        }
    }



    routing {
        graphQLPostRoute()
        graphQLGetRoute()
        graphQLSDLRoute()
        graphiQLRoute()
    }
}
