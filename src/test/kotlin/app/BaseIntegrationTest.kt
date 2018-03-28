package app

import app.config.ITConfig
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStoppedEvent
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [ITConfig::class],
    webEnvironment = WebEnvironment.NONE
)
@ActiveProfiles("test")
@ContextConfiguration(initializers = [BaseIntegrationTest.Initializer::class])
abstract class BaseIntegrationTest {
    companion object {
        init {
            System.setProperty("io.netty.noUnsafe", true.toString())
        }

        private class KGenericContainer(imageName: String) :
            GenericContainer<KGenericContainer>(imageName)

        private const val MONGO_PORT = 27017

        private val mongoContainer: KGenericContainer =
            KGenericContainer("mongo:latest")
                .withExposedPorts(MONGO_PORT)
    }

    class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(context: ConfigurableApplicationContext) {
            mongoContainer.start()

            println("mongo port: ${mongoContainer.getMappedPort(MONGO_PORT)}")

            TestPropertyValues.of(
                "spring.data.mongodb.uri=mongodb://localhost:${mongoContainer.getMappedPort(MONGO_PORT)}")
                .applyTo(context)

            context.addApplicationListener(ApplicationListener<ContextStoppedEvent> {
                mongoContainer.stop()
            })
        }
    }
}
