package coffee.michel.usermanager.persistence

import coffee.michel.usermanager.config.Database
import org.flywaydb.core.Flyway

fun migrateDatabase(config: Database) {
    configureFlyway(config)
        .migrate()
}

private fun configureFlyway(config: Database) = Flyway.configure()
    .dataSource(
        config.connectionString,
        config.username,
        config.password
    )
    .locations("classpath:db/migration")
    .validateOnMigrate(true)
    .load()