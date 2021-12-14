package exploring

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.isDirectory

class DirExplorer(private val filter: Filter, private val fileHandler: FileHandler) {
    interface FileHandler {
        fun handle(level: Int, path: String, file: Path)
    }

    interface Filter {
        fun interested(level: Int, path: String, file: Path): Boolean
    }

    fun explore(root: Path) {
        explore(0, "", root)
    }

    private fun explore(level: Int, path: String, file: Path) {
        if (file.isDirectory()) {
            for (child in Files.list(file)) {
                explore(level + 1, child.absolutePathString(), child)
            }
        } else {
            if (filter.interested(level, path, file)) {
                fileHandler.handle(level, path, file)
            }
        }
    }
}