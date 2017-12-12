package app.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
open class JacksonConfig {
	@Autowired
	private lateinit var mapper: ObjectMapper

	@Bean
	open fun kotlinModule() = KotlinModule()

	@Bean
	open fun javaTimeModule() = JavaTimeModule()

	@PostConstruct
	open fun init() {
		mapper.dateFormat = StdDateFormat()
	}
}
