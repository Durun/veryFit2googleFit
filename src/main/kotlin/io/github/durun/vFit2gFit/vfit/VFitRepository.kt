package io.github.durun.vFit2gFit.vfit

import java.io.File
import java.nio.file.Path

class VFitRepository(root: Path) {
	val syncDirPath = root.resolve("sync")
	val syncDir = syncDirPath.toFile()
	val syncLogFiles: List<File>
		get() {
			val logFiles = syncDir.listFiles { file -> file.extension == "log" }
					?.sortedBy { it.nameWithoutExtension }
					?: emptyList()
			return logFiles
		}
}