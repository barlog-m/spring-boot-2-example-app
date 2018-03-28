package app.customer

import org.bson.types.ObjectId
import java.math.BigDecimal
import java.time.LocalDate

fun generateCustomer() =
    Customer(
        id = ObjectId.get().toString(),
        name = "John Doe",
        balance = BigDecimal("12.0"),
        lastDeposit = LocalDate.of(2017, 11, 26)
    )
