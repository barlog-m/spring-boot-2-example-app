package app.initializer

import mu.KLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Component

@Component
open class DataInitializer(
    private val mongoTemplate: ReactiveMongoTemplate
) : ApplicationListener<ApplicationReadyEvent> {
    companion object : KLogging()

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { "initialize some data" }
    }
}
