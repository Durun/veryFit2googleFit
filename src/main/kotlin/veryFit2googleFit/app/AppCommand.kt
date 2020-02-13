package veryFit2googleFit.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import kotlinx.serialization.ImplicitReflectionSerializer
import veryFit2googleFit.vfit.HealthHeartRateItemLog
import veryFit2googleFit.vfit.HealthHeartRateLog
import veryFit2googleFit.vfit.VFitLogProviders
import veryFit2googleFit.vfit.fetchGroups
import java.nio.file.Path

object AppCommand : CliktCommand() {
	val veryfitRoot: Path by argument("veryfitRoot")
			.path(
					exists = true,
					fileOkay = false,
					folderOkay = true,
					readable = true
			)

	@ImplicitReflectionSerializer
	override fun run() {
		VFitLogProviders.fromFile(veryfitRoot)
				.fetchGroups<HealthHeartRateLog, HealthHeartRateItemLog>(from = "20200211")
				//.filter { (_, items) -> items.isNotEmpty() }
				.forEach { (rate, items) ->
					println(rate)
					println(items)
				}
	}
}