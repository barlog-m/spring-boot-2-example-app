package app.customer

import io.codearte.jfairy.Fairy
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom

fun generateCustomer() =
    Customer(
        name = Fairy.create().person().fullName,
        balance = BigDecimal(ThreadLocalRandom.current().nextInt(1, 12000)),
        lastDeposit = LocalDate.now()
            .plusDays(ThreadLocalRandom.current().nextLong(1, 99))
    )
