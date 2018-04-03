package app.customer

import java.math.BigDecimal
import java.time.LocalDate

fun generateCustomer() =
    Customer(
        name = "John Doe",
        balance = BigDecimal("12.0"),
        lastDeposit = LocalDate.of(2017, 11, 26)
    )
