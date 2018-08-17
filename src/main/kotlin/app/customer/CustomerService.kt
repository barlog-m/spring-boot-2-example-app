package app.customer

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
open class CustomerService(
    private val repository: CustomerRepository
) {
    open fun findByName(name: String): Flux<Customer> = repository.findByName(name)

    open fun save(customer: Customer): Mono<Customer> = repository.save(customer)

    open fun deleteById(id: String): Mono<Void> = repository.deleteById(id)
}
