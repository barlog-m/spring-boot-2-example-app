package app

import org.mockito.Mockito
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import org.springframework.test.web.reactive.server.WebTestClient.ListBodySpec
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec

inline fun <reified T> ResponseSpec.kExpectBody(): BodySpec<T, *> = expectBody(T::class.java)

inline fun <reified T> ResponseSpec.kExpectBodyList(): ListBodySpec<T> = expectBodyList(T::class.java)

fun <T> BodySpec<T, *>.kIsEqualTo(expected: T) {
    isEqualTo<Nothing?>(expected)
}

inline fun <reified T> kAnyObject(): T = kAnyObject(T::class.java)

inline fun <reified T> kAnyObject(t: Class<T>): T = Mockito.any(t)
