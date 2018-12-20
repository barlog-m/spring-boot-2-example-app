package app

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@ExtendWith(SpringExtension::class)
@JsonTest
@ActiveProfiles("test")
@Execution(SAME_THREAD)
class JacksonConfigTest {
    companion object {
        private val date = LocalDate.of(2018, 8, 14)
        private val time = LocalTime.of(16, 55, 30)
        private const val VISIT_TIME = "2018-08-14T16:55:30"
    }

    data class Visitor(
        val visitTime: LocalDateTime,
        val nullString: String? = null
    )

    @Autowired
    private lateinit var json: JacksonTester<Visitor>

    @Test
    fun serialize() {
        val visitor = Visitor(LocalDateTime.of(date, time))
        println(json.write(visitor))
        assertThat(json.write(visitor)).isEqualTo("visitor.json")
        assertThat(json.write(visitor)).isEqualToJson("visitor.json")
        assertThat(json.write(visitor)).hasJsonPathStringValue("@.visit_time")
        assertThat(json.write(visitor)).extractingJsonPathStringValue("@.visit_time")
            .isEqualTo(VISIT_TIME)
        assertThat(json.write(visitor)).doesNotHaveJsonPathValue("@.null_string")
    }

    @Test
    fun deserialize() {
        val content = "{\"visit_time\": \"$VISIT_TIME\"}"
        assertThat(json.parse(content))
            .isEqualTo(Visitor(LocalDateTime.of(date, time)))
        assertThat(json.parseObject(content).visitTime).isEqualTo(VISIT_TIME)
    }
}
