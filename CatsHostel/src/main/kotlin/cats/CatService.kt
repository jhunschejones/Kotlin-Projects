package cats

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface CatService {
    suspend fun create(name: String, age: Int?): Int
    suspend fun all(): List<Cat>
}

class CatServiceDB : CatService {
    override suspend fun create(name: String, age: Int?): Int {
        val id = transaction {
            Cats.insertAndGetId { cat ->
                cat[Cats.name] = name
                age?.let { cat[Cats.age] = it }
            }
        }
        return id.value
    }

    override suspend fun all(): List<Cat> = transaction { Cats.selectAll().map { row -> row.asCat() } }
}

private fun ResultRow.asCat() = Cat(
    this[Cats.id].value,
    this[Cats.name],
    this[Cats.age]
)

data class Cat(val id: Int, val name: String, val age: Int)