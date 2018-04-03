package app.customer

import org.bson.types.ObjectId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface CustomerRepository : ReactiveCrudRepository<Customer, ObjectId> {
    fun findBy(name: String): Flux<Customer>
}
