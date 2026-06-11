package louchez.emmanuel

import appModule
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing
import louchez.emmanuel.infrastruscture.database.initDatabase
import louchez.emmanuel.interfaces.graphql.UserMutation
import louchez.emmanuel.interfaces.graphql.UserQuery
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    initDatabase(
        url = environment.config.property("db.url").getString(),
        user = environment.config.property("db.user").getString(),
        dbPassword = environment.config.property("db.password").getString()
    )

    install(GraphQL) {
        schema {
            packages = listOf("louchez.emmanuel")
            queries = listOf(UserQuery(get()))
            mutations = listOf(UserMutation(get()))
        }
    }

    routing {
        graphQLPostRoute()
        graphQLGetRoute()
        graphQLSDLRoute()
        graphiQLRoute()
    }
}
