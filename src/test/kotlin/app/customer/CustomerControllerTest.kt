package app.customer

import app.kExpectBody
import app.kExpectBodyList
import app.kIsEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.beans.factory.annotation.Autowired
import org.mockito.BDDMockito.given
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@WebFluxTest(CustomerController::class)
class CustomerControllerTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var customerService: CustomerService

    private val customer = generateCustomer()

    @Test
    fun readById() {
        given(customerService.findById(customer.id))
            .willReturn(Mono.just(customer))

        webClient
            .get()
            .uri("/customer/${customer.id}")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk
            .kExpectBody<Customer>()
            .kIsEqualTo(customer)
    }

    @Test
    fun readByName() {
        given(customerService.findByName(customer.name))
            .willReturn(Flux.just(customer))

        webClient
            .get()
            .uri {
                it.path("/customer/by").queryParam("name", customer.name).build()
            }
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk
            .kExpectBodyList<Customer>()
            .kIsEqualTo(listOf(customer))
    }

    @Test
    fun create() {
        given(customerService.save(customer))
            .willReturn(Mono.just(customer.id))

        webClient
            .post()
            .uri("/customer")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(BodyInserters.fromObject(customer))
            .exchange()
            .expectStatus().isOk
            .kExpectBody<String>()
            .kIsEqualTo(customer.id)
    }

    @Test
    fun update() {
        given(customerService.save(customer))
            .willReturn(Mono.just(customer.id))

        webClient
            .put()
            .uri("/customer")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(BodyInserters.fromObject(customer))
            .exchange()
            .expectStatus().isOk
            .kExpectBody<String>()
            .kIsEqualTo(customer.id)
    }

    @Test
    fun delete() {
        given(customerService.deleteById(customer.id))
            .willReturn(Mono.empty())

        webClient
            .delete()
            .uri("/customer/${customer.id}")
            .exchange()
            .expectStatus().isOk
    }
}
