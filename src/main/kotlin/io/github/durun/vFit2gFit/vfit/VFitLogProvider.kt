package io.github.durun.vFit2gFit.vfit

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@UseExperimental(UnstableDefault::class)
val json = Json {
	strictMode = false
}

interface VFitLogProvider {
	fun getLineSequence(onDate: LocalDate): Sequence<String>
}

@ImplicitReflectionSerializer
inline fun <reified T : VFitLog> VFitLogProvider.fetch(onDate: LocalDate): Sequence<T> {
	return getLineSequence(onDate)
			.flatMap { it.extractVFitLogString(getKey<T>()) }
			.map { json.parseVFitLog<T>(it) }
}

@ImplicitReflectionSerializer
inline fun <reified G : VFitLog, reified T : VFitLog> VFitLogProvider.fetchGroups(onDate: LocalDate): Sequence<Pair<G, List<T>>> {
	return getLineSequence(onDate)
			.flatMap { line ->
				val groupStrings = line.extractVFitLogString(getKey<G>()).toList()
				val itemStrings = line.extractVFitLogString(getKey<T>()).toList()
				val groups = groupStrings.map { json.parseVFitLog<G>(it) }
				val items = itemStrings.map { json.parseVFitLog<T>(it) }
				(groups + items).asSequence()
			}    // giiigiigigiiigii...
			.splitByInstance<G, T, VFitLog>()
}

inline fun <reified H : T, reified B : T, T> Sequence<T>.splitByInstance(): Sequence<Pair<H, List<B>>> {
	return this.split { it is H }
			.mapNotNull {
				if ((it.first is H) && it.second.firstOrNull() is B) it as Pair<H, List<B>>
				else null
			}
}

fun <T> Sequence<T>.split(predicate: (T) -> Boolean): Sequence<Pair<T?, List<T>>> {
	val seq = this.toList()
	return sequence {
		var remain = seq
		while (remain.isNotEmpty()) {
			val splitter = remain.first()    // g
			remain = remain.drop(1)
			val items = remain.dropWhile(predicate).takeWhile { !predicate(it) }.toList()    // iii
			remain = remain.drop(items.size)
			//assert(predicate(splitter))
			yield(splitter to items)
		}
	}
}

object VFitLogProviders {
	fun fromFile(vfitRoot: Path): VFitLogProvider = FileVFitLogProvider(vfitRoot)
}

private class FileVFitLogProvider(private val repository: VFitRepository) : VFitLogProvider {
	constructor(vfitRoot: Path) : this(VFitRepository(vfitRoot))

	override fun getLineSequence(onDate: LocalDate): Sequence<String> {
		val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
		val files = repository.syncLogFiles.filter {
			it.nameWithoutExtension == formatter.format(onDate)
		}
		return files.asSequence()
				.flatMap {
					it.readLines().asSequence()
				}
	}
}