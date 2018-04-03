package app.customer

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CustomerService(
    private val repository: CustomerRepository
) {
    fun findByName(name: String): Flux<Customer> = repository.findByName(name)

    fun save(customer: Customer): Mono<Customer> = repository.save(customer)

    fun deleteById(id: ObjectId): Mono<Void> = repository.deleteById(id)
}
