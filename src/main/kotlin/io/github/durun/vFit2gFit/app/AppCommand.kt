package io.github.durun.vFit2gFit.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import io.github.durun.vFit2gFit.model.HeartRate
import io.github.durun.vFit2gFit.model.Sleep
import io.github.durun.vFit2gFit.vfit.*
import io.github.durun.vFit2gFit.writer.db.DBWriter
import kotlinx.serialization.ImplicitReflectionSerializer
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.toJavaDuration

object AppCommand : CliktCommand() {
	val veryfitRoot: Path by argument("veryfitRoot")
			.path(
					exists = true,
					fileOkay = false,
					folderOkay = true,
					readable = true
			)
	val dbPath: Path by argument("dbPath")
			.path(folderOkay = false)

	var itemCount = 0

	@ExperimentalTime
	@ImplicitReflectionSerializer
	override fun run() {
		val writer = DBWriter.of(dbPath)
		(0..100).map { (LocalDateTime.now() - it.days.toJavaDuration()).toLocalDate() }
				.forEach {
					writer.recordHeartRates(it)
					writer.recordSleeps(it)
				}
		println("Total items: $itemCount")
	}

	@ExperimentalTime
	@ImplicitReflectionSerializer
	private fun DBWriter.recordHeartRates(onDate: LocalDate) {
		val logs = VFitLogProviders.fromFile(veryfitRoot)
				.fetchGroups<HealthHeartRateLog, HealthHeartRateItemLog>(onDate)
				.lastOrNull()
		val heartRates = logs
				?.let { (rate, item) ->
					itemCount += item.size
					println("$onDate HeartRate items: ${item.size}")
					HeartRate.of(rate, item)
				}
				?: emptyList()
		this.write(heartRates)
	}

	@ExperimentalTime
	@ImplicitReflectionSerializer
	private fun DBWriter.recordSleeps(onDate: LocalDate) {
		val logs = VFitLogProviders.fromFile(veryfitRoot)
				.fetchGroups<HealthSleepLog, HealthSleepItemLog>(onDate)
				.lastOrNull()
		val sleeps = logs
				?.let { (sleep, items) ->
					Sleep.of(sleep, items)
				}
				?.let { println(it) }

	}
}