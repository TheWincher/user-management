package louchez.emmanuel.di

import louchez.emmanuel.infrastruscture.database.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initTestDatabase() {
    Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        driver = "org.h2.Driver"
    )
    transaction {
        SchemaUtils.create(UserTable)
    }
}