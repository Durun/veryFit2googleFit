package veryFit2googleFit.vfit

import veryFit2googleFit.model.HeartRate
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.toJavaDuration

@ExperimentalTime
fun HeartRate.Companion.of(rateLog: HealthHeartRateLog, itemLogs: Iterable<HealthHeartRateItemLog>): List<HeartRate> {
	val startTime = rateLog.date + 1.minutes

	var currentTime = startTime
	return itemLogs.toList().map { log ->
		currentTime += log.offsetMinute.minutes
		HeartRate.of(bpm = log.HeartRaveValue, timeAt = currentTime)
	}
}

@ExperimentalTime
internal operator fun LocalDateTime.plus(duration: Duration): LocalDateTime = this + duration.toJavaDuration()