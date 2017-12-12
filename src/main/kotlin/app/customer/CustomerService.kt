package app.customer

import org.springframework.stereotype.Service

@Service
class CustomerService(
	private val repository: CustomerRepository
) {
	fun findBy(name: String) = repository.findBy(name)

	fun save(customer: Customer) = repository.save(customer)

	fun delete(id: String) = repository.deleteById(id)
}
