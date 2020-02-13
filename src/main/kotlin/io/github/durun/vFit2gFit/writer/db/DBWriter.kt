package io.github.durun.vFit2gFit.writer.db

import io.github.durun.vFit2gFit.model.HeartRate
import io.github.durun.vFit2gFit.writer.db.table.HeartRateTable
import io.github.durun.vFit2gFit.writer.db.table.HeartRateTable.bpm
import io.github.durun.vFit2gFit.writer.db.table.HeartRateTable.timeAt
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
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
				HeartRateTable
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
}