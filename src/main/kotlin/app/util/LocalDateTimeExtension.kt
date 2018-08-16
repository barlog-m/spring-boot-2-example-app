package app.util

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun LocalDateTime.truncate(): LocalDateTime = this.truncatedTo(ChronoUnit.MILLIS)

fun LocalTime.truncate(): LocalTime = this.truncatedTo(ChronoUnit.MILLIS)
