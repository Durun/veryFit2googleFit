package io.github.durun.vFit2gFit.writer.db

import io.github.durun.vFit2gFit.model.HeartRate
import io.github.durun.vFit2gFit.model.Sleep
import io.github.durun.vFit2gFit.writer.db.HeartRateTable.bpm
import io.github.durun.vFit2gFit.writer.db.HeartRateTable.timeAt
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Path

class DBWriter private constructor(
		private val db: Database
) {
	companion object {
		fun of(dbFile: File): DBWriter = DBWriter(SQLiteDB.of(dbFile))
		fun of(dbPath: Path): DBWriter = DBWriter(SQLiteDB.of(dbPath))

		private val tables = listOf(
				HeartRateTable,
				SleepTable,
				SleepDetailTable
		)
	}

	init {
		initTables()
	}

	private fun initTables() {
		transaction(db) {
			tables.forEach {
				SchemaUtils.createMissingTablesAndColumns(it)
			}
		}
	}

	fun write(heartRates: Iterable<HeartRate>) {
		transaction(db) {
			HeartRateTable.batchInsert(heartRates, ignore = true) {
				this[timeAt] = it.timeAt
				this[bpm] = it.bpm
			}
		}
	}

	fun write(sleep: Sleep) {
		transaction(db) {
			SleepTable.insertIgnore {
				it[SleepTable.timeFrom] = sleep.timeFrom
				it[SleepTable.timeTo] = sleep.timeTo
			}

			SleepDetailTable.batchInsert(sleep.statuses, ignore = true) {
				this[SleepDetailTable.timeFrom] = it.timeRange.first
				this[SleepDetailTable.timeTo] = it.timeRange.second
				this[SleepDetailTable.depth] = it.depth.value
			}
		}
	}
}