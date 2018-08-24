package app.customer

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
open class CustomerService(
    private val repository: CustomerRepository
) {
    open fun findById(id: String): Mono<Customer> =
        repository.findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(
                HttpStatus.NOT_FOUND, "customer with id:'$id' not found")))

    open fun findByName(name: String): Flux<Customer> =
        repository.findByName(name)
            .switchIfEmpty(Flux.error(ResponseStatusException(
                    HttpStatus.NOT_FOUND, "customers with name: '$name' not found")))

    open fun save(customer: Customer): Mono<String> =
        repository.save(customer).map { it.id }

    open fun deleteById(id: String): Mono<Void> = repository.deleteById(id)
}
