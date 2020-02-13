package io.github.durun.vFit2gFit.vfit

import io.github.durun.vFit2gFit.model.HeartRate
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.toJavaDuration

@ExperimentalTime
internal operator fun LocalDateTime.plus(duration: Duration): LocalDateTime = this + duration.toJavaDuration()

@ExperimentalTime
fun HeartRate.Companion.of(rateLog: HealthHeartRateLog, itemLogs: Iterable<HealthHeartRateItemLog>): List<HeartRate> {
	val startTime = rateLog.date + rateLog.startTime.minutes

	var currentTime = startTime
	return itemLogs.toList().map { log ->
		currentTime += log.offsetMinute.minutes
		HeartRate.of(bpm = log.HeartRaveValue, timeAt = currentTime)
	}
}
