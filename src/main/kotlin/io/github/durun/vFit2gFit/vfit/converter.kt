package io.github.durun.vFit2gFit.vfit

import io.github.durun.vFit2gFit.model.HeartRate
import io.github.durun.vFit2gFit.model.Sleep
import io.github.durun.vFit2gFit.model.Sleep.Status.Companion.SleepDepth
import java.time.LocalDateTime
import kotlin.time.*

@ExperimentalTime
internal operator fun LocalDateTime.plus(duration: Duration): LocalDateTime = this + duration.toJavaDuration()

@ExperimentalTime
internal operator fun LocalDateTime.minus(duration: Duration): LocalDateTime = this - duration.toJavaDuration()

@ExperimentalTime
fun HeartRate.Companion.of(rateLog: HealthHeartRateLog, itemLogs: Iterable<HealthHeartRateItemLog>): List<HeartRate> {
	val startTime = rateLog.date + rateLog.startTime.minutes

	var currentTime = startTime
	return itemLogs.toList().map { log ->
		currentTime += log.offsetMinute.minutes
		HeartRate.of(bpm = log.HeartRaveValue, timeAt = currentTime)
	}
}

@ExperimentalTime
fun Sleep.Companion.of(sleepLog: HealthSleepLog, itemLogs: Iterable<HealthSleepItemLog>): Sleep {
	val endTime = sleepLog.date + sleepLog.sleepEndedTimeH.hours + sleepLog.sleepEndedTimeM.minutes
	val startTime = endTime - sleepLog.totalSleepMinutes.minutes

	var currentTime = startTime
	val logs = itemLogs.toList()
	val statuses = logs.mapIndexed { index, log ->
		currentTime += log.offsetMinute.minutes
		val depth = when (log.sleepStatus) {
			1 -> SleepDepth.AWAKE
			2 -> SleepDepth.LIGHT
			3 -> SleepDepth.DEEP
			else -> throw IllegalArgumentException("sleepStatus is not 1 or 2 or 3.")
		}
		val nextTime =
				if (index < logs.lastIndex) currentTime + logs[index].offsetMinute.minutes
				else endTime
		Sleep.Status.of(time = currentTime to nextTime, depth = depth)
	}
	return Sleep.of(time = startTime to endTime, statuses = statuses)
}