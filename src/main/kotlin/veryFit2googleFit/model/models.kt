package veryFit2googleFit.model

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