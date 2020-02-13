package veryFit2googleFit.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.serialization.ImplicitReflectionSerializer
import veryFit2googleFit.model.HeartRate
import veryFit2googleFit.vfit.*
import veryFit2googleFit.writer.db.DBWriter
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
					writer.recordOn(it)
				}
		println("Total items: $itemCount")
	}

	@ExperimentalTime
	@ImplicitReflectionSerializer
	private fun DBWriter.recordOn(onDate: LocalDate) {
		val logs = VFitLogProviders.fromFile(veryfitRoot)
				.fetchGroups<HealthHeartRateLog, HealthHeartRateItemLog>(onDate)
				.lastOrNull()
		val heartRates = logs
				?.let { (rate, item) ->
					itemCount += item.size
					println("$onDate items: ${item.size}")
					HeartRate.of(rate, item)
				}
				?: emptyList()
		this.write(heartRates)
	}
}