package app

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@ExtendWith(SpringExtension::class)
@JsonTest
@ActiveProfiles("test")
class JacksonConfigTest {
    companion object {
        private val zone = ZoneId.of("+03:00")
        private val date = LocalDate.of(2018, 8, 14)
        private val time = LocalTime.of(16, 55, 30)
        private const val VISIT_TIME = "2018-08-14T16:55:30+03:00"
    }

    data class Visitor(
        val visitTime: ZonedDateTime,
        val nullString: String? = null
    )

    @Autowired
    private lateinit var json: JacksonTester<Visitor>

    @Test
    fun serialize() {
        val visitor = Visitor(ZonedDateTime.of(date, time, zone))
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
            .isEqualTo(Visitor(ZonedDateTime.of(date, time, zone)))
        assertThat(json.parseObject(content).visitTime).isEqualTo(VISIT_TIME)
    }
}
