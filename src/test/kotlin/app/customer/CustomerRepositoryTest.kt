package app.customer

import app.BaseIntegrationTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CustomerRepositoryTest : BaseIntegrationTest() {
	@Autowired
	private lateinit var repository: CustomerRepository

	private val customer = generateCustomer()

	@Test
	fun findByName() {
		repository
			.save(customer)
			.then()
			.block()

		val result = repository
			.findBy(customer.name)
			.collectList()
			.block()

		assertTrue(result.isNotEmpty())

		val readCustomer = result.first { it.id == customer.id }
		assertEquals(customer, readCustomer)

		repository
			.delete(customer)
			.then()
			.block()
	}

	@Test
	fun save() {
		repository
			.save(customer)
			.then()
			.block()

		val savedCustomer = repository
			.findById(customer.id)
			.block()

		assertEquals(customer, savedCustomer)

		repository
			.delete(customer)
			.then()
			.block()
	}

	@Test
	fun update() {
		repository
			.save(customer)
			.then()
			.block()

		val updatedCustomer = customer.copy(
			name = "Jane Doe"
		)

		repository
			.save(updatedCustomer)
			.then()
			.block()

		val savedCustomer = repository
			.findById(customer.id)
			.block()

		assertEquals(customer.id, savedCustomer.id)
		assertEquals("Jane Doe", savedCustomer.name)

		repository
			.deleteById(customer.id)
			.then()
			.block()
	}

	@Test
	fun delete() {
		repository
			.save(customer)
			.then()
			.block()

		repository
			.delete(customer)
			.then()
			.block()

		val result = repository
			.findBy(customer.name)
			.collectList()
			.block()

		assertTrue(result.isEmpty())
	}
}
