package exploring

import java.nio.file.Path

class JavaFilter : DirExplorer.Filter {
    override fun interested(level: Int, path: String, file: Path): Boolean {
        return !path.contains("/test/") && path.endsWith(".java")
    }
}