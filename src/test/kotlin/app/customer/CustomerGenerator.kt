package app.customer

import app.util.truncate
import io.codearte.jfairy.Fairy
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

fun generateCustomer() =
    Customer(
        name = Fairy.create().person().fullName,
        balance = BigDecimal(ThreadLocalRandom.current().nextInt(1, 12000)),
        lastDeposit = LocalDateTime.now().truncate()
            .plusDays(ThreadLocalRandom.current().nextLong(1, 99))
    )
