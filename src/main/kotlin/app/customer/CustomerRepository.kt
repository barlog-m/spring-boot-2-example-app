package app.customer

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface CustomerRepository : ReactiveCrudRepository<Customer, String> {
    fun findByName(name: String): Flux<Customer>
}
