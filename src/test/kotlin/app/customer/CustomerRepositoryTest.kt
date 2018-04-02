package app.customer

import app.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class CustomerRepositoryTest : BaseIntegrationTest() {
    @Autowired
    private lateinit var repository: CustomerRepository

    private val customer = generateCustomer()

    @Test
    fun findByName() {
        StepVerifier.create(
            repository
                .save(customer)

                .then(repository
                    .findBy(customer.name)
                    .collectList()
                ))
            .assertNext {
                it.isNotEmpty() &&
                    it.first { it.id == customer.id } != null
            }
            .verifyComplete()

        repository
            .delete(customer)
            .then()
            .block()
    }

    @Test
    fun save() {
        StepVerifier.create(
            repository
                .save(customer)
                .then(
                    repository
                        .findById(customer.id)
                ))
            .expectNext(customer)
            .verifyComplete()

        repository
            .delete(customer)
            .then()
            .block()
    }

    @Test
    fun update() {
        val newName = "Jane Doe"
        StepVerifier.create(
            repository
                .save(customer)
                .map {
                    it.copy(
                        name = newName
                    )
                }
                .flatMap {
                    repository.save(it).thenReturn(it)
                }
                .flatMap {
                    repository
                        .findById(customer.id)
                        .thenReturn(it.name)
                })
            .expectNext(newName)
            .verifyComplete()

        repository
            .deleteById(customer.id)
            .then()
            .block()
    }

    @Test
    fun delete() {
        StepVerifier.create(
            repository
                .save(customer)
                .then(
                    repository
                        .delete(customer)
                )
                .then(
                    repository
                        .findBy(customer.name)
                        .collectList()
                ))
            .assertNext { it.isEmpty() }
            .verifyComplete()
    }
}
