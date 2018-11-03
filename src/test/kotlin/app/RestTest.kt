package app

import org.junit.jupiter.api.Tag
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Tag("rest")
@ActiveProfiles("test")
@Import(value = [JacksonAutoConfiguration::class, CodecsAutoConfiguration::class])
annotation class RestTest
