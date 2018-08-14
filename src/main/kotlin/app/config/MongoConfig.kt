package app.config

import org.bson.BsonDateTime
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import java.time.ZonedDateTime
import java.util.Date

@EnableReactiveMongoRepositories(basePackages = ["app"])
@Configuration
open class MongoConfig {
    @Bean
    open fun customConversions() = MongoCustomConversions(
        listOf(DateToZonedDateTimeConverter)
    ) {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(DateToZonedDateTimeConverter.INSTANCE);
        converters.add( ZonedDateTimeToDateConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    object DateToZonedDateTimeConverter: Converter<Date, ZonedDateTime> {
        override fun convert(source: Date): ZonedDateTime {
        }
    }

    object ZonedDateTimeToDateConverter: Converter<ZonedDateTime, Date> {
        override fun convert(source: ZonedDateTime?): Date {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
