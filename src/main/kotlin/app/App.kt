package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
open class App

fun main(vararg args: String) {
    SpringApplicationBuilder(App::class.java)
        .registerShutdownHook(true).run(*args)
}
