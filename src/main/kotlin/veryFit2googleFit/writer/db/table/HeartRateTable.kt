package veryFit2googleFit.writer.db.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime

object HeartRateTable : Table("heartRate") {
	val timeAt = datetime("timeAt")

	val bpm: Column<Int> = integer("bpm")
	override val primaryKey: PrimaryKey? = PrimaryKey(timeAt)
}