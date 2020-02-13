package io.github.durun.vFit2gFit.vfit

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse


@ImplicitReflectionSerializer
inline fun <reified T : VFitLog> Json.parseVFitLog(str: String): T {
	val dayExpr = "[A-Z][a-z]{2}"
	//println("formatting: $str")
	val jsonStr = str
			.replace(Regex(".*?(\\{.*\\})$")) { it.groupValues[1] }
			.replace("{", "{\"")
			.replace("=", "\":")
			.replace(", ", ", \"")
			.replace(Regex("$dayExpr $dayExpr \\d{2} \\d{2}:\\d{2}:\\d{2} GMT\\+\\d{2}:\\d{2} \\d{4}")) { "\"${it.value}\"" }
	//println("parsing: $jsonStr")
	return parse(jsonStr)
}

fun String.extractVFitLogString(key: String): Sequence<String> {
	//if (this.matches(Regex(".*\\{.*\\}.*"))) println("extracting: $this")
	//return Regex("$key\\{[^=,\\{\\} ]+=[^=,\\{\\} ]+(, ?[^=,\\{\\} ]+=[^=,\\{\\} ]+)*\\}")
	return Regex("$key\\{.*?\\}")
			.findAll(this)
			.map { it.value }
}