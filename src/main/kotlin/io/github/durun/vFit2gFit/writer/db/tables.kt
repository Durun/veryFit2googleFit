package io.github.durun.vFit2gFit.writer.db

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime

object HeartRateTable : Table("heartRate") {
	val timeAt = datetime("timeAt")

	val bpm: Column<Int> = integer("bpm")
	override val primaryKey: PrimaryKey? = PrimaryKey(timeAt)
}

object SleepTable : Table("sleep") {
	val timeFrom = datetime("timeFrom")
	val timeTo = datetime("timeTo")
	override val primaryKey: PrimaryKey? = PrimaryKey(timeFrom, timeTo)
}

object SleepDetailTable:Table("sleepDetail") {
	val timeFrom = datetime("timeFrom")
	val timeTo = datetime("timeTo")

	val depth = integer("depth")
	override val primaryKey: PrimaryKey? = PrimaryKey(timeFrom, timeTo)
}