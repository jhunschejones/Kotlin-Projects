package cats

import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

interface CatService {
    suspend fun create(name: String, age: Int?): Int
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
}
