package app.customer

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.mockito.Mockito.`when` as wh

class CustomerServiceTest {
	private val repository = mock(CustomerRepository::class.java)
	private val customer = generateCustomer()

	@Test
	fun findBy() {
		wh(repository.findBy(customer.name)).thenReturn(Flux.just(customer))
		repository.findBy(customer.name)
		verify(repository, times(1)).findBy(customer.name)
	}

	@Test
	fun save() {
		wh(repository.save(customer)).thenReturn(Mono.just(customer))
		repository.save(customer)
		verify(repository, times(1)).save(customer)
	}

	@Test
	fun delete() {
		repository.deleteById(customer.id)
		verify(repository, times(1)).deleteById(customer.id)
	}
}
