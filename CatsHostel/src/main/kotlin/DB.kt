import org.jetbrains.exposed.dao.IntIdTable

object Cats: IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val age = integer("age").default(0)
}
