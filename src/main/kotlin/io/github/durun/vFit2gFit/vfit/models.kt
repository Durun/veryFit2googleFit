package io.github.durun.vFit2gFit.vfit

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
	private val df: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")

	override val descriptor: SerialDescriptor =
			StringDescriptor.withName("WithCustomDefault")

	override fun serialize(encoder: Encoder, obj: Date) {
		encoder.encodeString(df.format(obj))
	}

	override fun deserialize(decoder: Decoder): Date {
		return df.parse(decoder.decodeString())
	}
}

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
	private val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy")

	override val descriptor: SerialDescriptor =
			StringDescriptor.withName("WithCustomDefault")

	override fun serialize(encoder: Encoder, obj: LocalDateTime) {
		encoder.encodeString(formatter.format(obj))
	}

	override fun deserialize(decoder: Decoder): LocalDateTime {
		return LocalDateTime.parse(decoder.decodeString(), formatter)
	}
}

interface VFitLog

@Serializable
data class HealthHeartRateLog(
		val rateDataId: Int,
		val dId: Int,
		val year: Int,
		val month: Int,
		val day: Int,
		val startTime: Int,
		val silentHeart: Int,
		val warmUpThreshold: Int,
		val burn_fat_threshold: Int,
		val aerobic_threshold: Int,
		val anaerobicThreshold: Int,
		val limit_threshold: Int,
		val warmUpMins: Int,
		val burn_fat_mins: Int,
		val aerobic_mins: Int,
		val anaerobicMins: Int,
		val limit_mins: Int,
		val UserMaxHr: Int,
		val Range1: Int,
		val Range2: Int,
		val Range3: Int,
		val Range4: Int,
		val Range5: Int,
		@Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime
) : VFitLog

@Serializable
data class HealthHeartRateItemLog(
		val heartRateDataId: Int,
		val dId: Int,
		val year: Int,
		val month: Int,
		val day: Int,
		val offsetMinute: Int,
		val HeartRaveValue: Int,
		@Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime
) : VFitLog