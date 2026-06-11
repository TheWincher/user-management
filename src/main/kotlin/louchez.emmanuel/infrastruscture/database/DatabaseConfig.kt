package louchez.emmanuel.infrastruscture.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDatabase(url: String, user: String, dbPassword: String) {
    val config = HikariConfig().apply {
        jdbcUrl = url
        username = user
        driverClassName = "org.postgresql.Driver"
        password = dbPassword
    }

    Database.connect(HikariDataSource(config))
    transaction { SchemaUtils.create(UserTable) }
}
