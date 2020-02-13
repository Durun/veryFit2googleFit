package io.github.durun.vFit2gFit.writer.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.sqlite.JDBC
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Connection
import kotlin.reflect.jvm.jvmName

object SQLiteDB {
	fun of(file: File): Database {
		if (!file.isFile) throw IllegalArgumentException("file is not file.")
		val db = Database.connect("jdbc:sqlite:${file.absolutePath}", JDBC::class.jvmName)
		TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
		return db
	}

	fun of(path: Path): Database {
		val file = path.toFile().takeIf { it.isFile } ?: Files.createFile(path).toFile()
		return of(file)
	}
}