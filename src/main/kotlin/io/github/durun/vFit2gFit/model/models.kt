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

	data class Status(
			val timeAt: LocalDateTime,
			val depth: SleepDepth
	) {
		enum class SleepDepth {
			AWAKE, LIGHT, DEEP
		}
	}
}
