package app.customer

import app.BaseIntegrationTest
import app.generator.generateCustomer
import io.codearte.jfairy.Fairy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class CustomerRepositoryTest : BaseIntegrationTest() {
    @Autowired
    private lateinit var repository: CustomerRepository

    @Test
    fun findById() {
        val referenceCustomer = generateCustomer()
        repository.save(referenceCustomer).block()

        StepVerifier.create(
            repository
                .findById(referenceCustomer.id))
            .assertNext { customer ->
                assertEquals(referenceCustomer.id, customer.id)
            }
            .verifyComplete()

        repository.delete(referenceCustomer).block()
    }

    @Test
    fun findByName() {
        val customer = generateCustomer()
        repository.save(customer).block()

        StepVerifier.create(
            repository
                .findByName(customer.name)
                .collectList())
            .assertNext { customers ->
                assertAll({
                    assertTrue(customers.isNotEmpty())
                    assertNotNull(customers.first { it.id == customer.id })
                })
            }
            .verifyComplete()

        repository.delete(customer).block()
    }

    @Test
    fun save() {
        val customer = generateCustomer()
        repository.save(customer).block()

        StepVerifier.create(
            repository
                .findById(customer.id))
            .expectNext(customer)
            .verifyComplete()

        repository.delete(customer).block()
    }

    @Test
    fun update() {
        val customer = generateCustomer()
        repository.save(customer).block()

        val newName = Fairy.create().person().fullName
        StepVerifier.create(
            repository.save(customer.copy(name = newName))
                .flatMap {
                    repository
                        .findById(customer.id)
                        .thenReturn(it.name)
                })
            .expectNext(newName)
            .verifyComplete()

        repository.deleteById(customer.id).block()
    }

    @Test
    fun delete() {
        val customer = generateCustomer()
        repository.save(customer).block()

        StepVerifier.create(
            repository
                .delete(customer)
                .then(
                    repository
                        .findByName(customer.name)
                        .collectList()
                ))
            .assertNext {
                assertTrue(it.isEmpty())
            }
            .verifyComplete()
    }
}
