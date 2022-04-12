package coffee.michel.usermanager

import coffee.michel.usermanager.config.Configuration
import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.SubjectPersistence
import coffee.michel.usermanager.persistence.UserGroupPersistence
import coffee.michel.usermanager.persistence.migrateDatabase
import com.sksamuel.hoplite.ConfigLoader
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.vendors.DatabaseDialect

fun main() {
    val configuration = loadConfiguration()

    migrateDatabase(configuration.database)
    Database.connect(
        url = configuration.database.connectionString,
        driver = configuration.database.driver,
        user = configuration.database.username,
        password = configuration.database.password,
        databaseConfig = DatabaseConfig.invoke {
            useNestedTransactions = true
        },
    )

    val ugp = UserGroupPersistence.instance()
    val sp = SubjectPersistence.instance()

    val ug1 = ugp.create(UserGroup(null, "admin"))
    val ug2 = ugp.create(UserGroup(null, "user"))

    sp.create(Subject(null, "thomas", "111213", setOf(ug1)))
    sp.create(Subject(null, "abders", "111213", setOf(ug1, ug2)))
    sp.create(Subject(null, "herman", "111213", setOf(ug2)))

    sp.findAll().forEach { println(it) }

    ugp.delete(ug1.id!!)

    sp.findAll().forEach { println(it) }
}

fun loadConfiguration(): Configuration =
    ConfigLoader().loadConfigOrThrow("/configuration-pqsl.yml")