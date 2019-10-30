package app

import app.config.ITConfig
import mu.KLogging
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy

@SpringBootTest(
    classes = [ITConfig::class],
    webEnvironment = WebEnvironment.NONE
)
@ActiveProfiles("test")
@ContextConfiguration(initializers = [BaseIntegrationTest.Initializer::class])
@Execution(SAME_THREAD)
abstract class BaseIntegrationTest {
    companion object : KLogging() {
        init {
            System.setProperty("io.netty.noUnsafe", true.toString())
        }

        private class KGenericContainer(imageName: String) :
            GenericContainer<KGenericContainer>(imageName)

        private const val MONGO_PORT = 27017

        private val mongoContainer: KGenericContainer =
            KGenericContainer("mongo:latest")
                .withExposedPorts(MONGO_PORT)
                .waitingFor(LogMessageWaitStrategy().withRegEx(".*waiting for connections on port 27017\n"))
    }

    class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(context: ConfigurableApplicationContext) {
            mongoContainer.start()

            logger.info { "mongo port: ${mongoContainer.getMappedPort(MONGO_PORT)}" }

            TestPropertyValues.of(
                "spring.data.mongodb.uri=mongodb://localhost:${mongoContainer.getMappedPort(MONGO_PORT)}")
                .applyTo(context)

            context.addApplicationListener(ApplicationListener { _: ContextClosedEvent ->
                mongoContainer.stop()
                logger.info { "mongo container stop" }
            })

            context.registerShutdownHook()
        }
    }
}
