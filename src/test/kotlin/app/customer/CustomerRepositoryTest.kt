package app.customer

import app.BaseIntegrationTest
import app.generator.generateCustomer
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
	}
}
