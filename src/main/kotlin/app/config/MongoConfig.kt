package app.config

import com.mongodb.WriteConcern
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
open class MongoConfig {
    @Bean
    open fun writeConcertMongoClientCustomizer() = MongoClientSettingsBuilderCustomizer {
        clientSettingsBuilder ->
        clientSettingsBuilder.writeConcern(
            WriteConcern.W1
                .withWTimeout(5, TimeUnit.SECONDS)
                .withJournal(true)
        )
    }
}
