package louchez.emmanuel

import appModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import louchez.emmanuel.infrastruscture.database.initDatabase
import louchez.emmanuel.interfaces.graphql.configureGraphQL
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
    configureGraphQL()
}
