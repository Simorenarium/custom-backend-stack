package coffee.michel.usermanager.config

data class Database(
    val connectionString: String,
    val driver: String,
    val username: String,
    val password: String,
    )
