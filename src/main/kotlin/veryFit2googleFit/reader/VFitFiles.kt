package veryFit2googleFit.reader

import java.io.File
import java.nio.file.Path

class VFitFiles(root: Path) {
    val syncDirPath = root.resolve("sync")
    val syncDir = syncDirPath.toFile()
    val syncLogFiles: Iterable<File>
        get() = syncDir.listFiles { file -> file.extension == "log" }?.asIterable() ?: emptyList()
}