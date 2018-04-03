package app.customer

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate

@Document
data class Customer(
    @Id val id: ObjectId = ObjectId.get(),
    val name: String,
    val balance: BigDecimal,
    val lastWithdraw: LocalDate? = null,
    val lastDeposit: LocalDate
)
