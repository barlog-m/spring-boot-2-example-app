package app.customer

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomerRepository : ReactiveCrudRepository<Customer, String> {
	fun findBy(name: String): Flux<Customer>
	fun findBy(name: Mono<String>): Flux<Customer>
}
