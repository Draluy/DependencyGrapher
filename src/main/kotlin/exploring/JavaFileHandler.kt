package exploring

import java.nio.file.Path

class JavaFileHandler : DirExplorer.FileHandler{

    val relevantFiles  = ArrayList<Path>()

    override fun handle(level: Int, path: String, file: Path) {
        relevantFiles.add(file)
    }
}