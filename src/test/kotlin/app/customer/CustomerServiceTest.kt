package app.customer

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CustomerServiceTest {
	private val repository = mock(CustomerRepository::class.java)
	private val customer = generateCustomer()

	@Test
	fun findBy() {
		given(repository.findBy(customer.name)).willReturn(Flux.just(customer))
		repository.findBy(customer.name)
		verify(repository, times(1)).findBy(customer.name)
	}

	@Test
	fun save() {
		given(repository.save(customer)).willReturn(Mono.just(customer))
		repository.save(customer)
		verify(repository, times(1)).save(customer)
	}

	@Test
	fun deleteById() {
		repository.deleteById(customer.id)
		verify(repository, times(1)).deleteById(customer.id)
	}
}
