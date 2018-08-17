package app.customer

import org.bson.types.ObjectId
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/customer")
class CustomerController(
    private val service: CustomerService
) {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(@RequestBody customer: Customer): Mono<Void> =
        service.save(customer).then()

    @GetMapping(
        path = ["/by"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
    )
    fun readByName(@RequestParam name: String): Flux<Customer> =
        service.findByName(name)

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(@RequestBody customer: Customer): Mono<Void> =
        service.save(customer).then()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: ObjectId): Mono<Void> =
        service.deleteById(id.toHexString())
}
