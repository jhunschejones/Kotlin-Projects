import org.jetbrains.exposed.sql.Database

object DB {
    private val dbHost = "localhost"
    private val dbPort = 5432
    private val dbName = "cats_hostel_development"
    private val dbUser = "cats_hostel_development_user"
    private val dbPassword = "very_secret"

    fun connect() = Database.connect(
        "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}
