package io.github.durun.vFit2gFit.model

import java.time.LocalDateTime

data class HeartRate(
		val timeAt: LocalDateTime,
		val bpm: Int
) {
	companion object {
		fun of(bpm: Int, timeAt: LocalDateTime): HeartRate {
			return HeartRate(timeAt = timeAt, bpm = bpm)
		}
	}
}

data class Sleep private constructor(
		val timeFrom: LocalDateTime,
		val timeTo: LocalDateTime,
		val statuses: List<Status>
) {
	companion object {
		fun of(time: Pair<LocalDateTime, LocalDateTime>, statuses: List<Status>): Sleep {
			return Sleep(timeFrom = time.first, timeTo = time.second, statuses = statuses)
		}
	}

	data class Status private constructor(
			val timeRange: Pair<LocalDateTime, LocalDateTime>,
			val depth: SleepDepth
	) {
		companion object {
			fun of(time: Pair<LocalDateTime, LocalDateTime>, depth: SleepDepth): Status {
				return Status(timeRange = time, depth = depth)
			}

			enum class SleepDepth(val value: Int) {
				AWAKE(1), LIGHT(2), DEEP(3)
			}
		}
	}
}
