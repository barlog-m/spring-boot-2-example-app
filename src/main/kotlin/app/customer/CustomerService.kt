package app.customer

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
open class CustomerService(
	private val repository: CustomerRepository
) {
	open fun findBy(name: String): Flux<Customer> = repository.findBy(name)

	open fun save(customer: Customer): Mono<Customer> = repository.save(customer)

	open fun deleteById(id: String): Mono<Void> = repository.deleteById(id)
}
