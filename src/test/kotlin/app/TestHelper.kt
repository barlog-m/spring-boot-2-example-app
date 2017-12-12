package app

import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec

inline fun <reified T> ResponseSpec.kExpectBody() : BodySpec<T, *> = expectBody(T::class.java)

fun <T> BodySpec<T, *>.kIsEqualTo(expected: T) = {
	isEqualTo<Nothing?>(expected)
}
