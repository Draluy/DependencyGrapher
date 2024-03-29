import exploring.DirExplorer
import exploring.JavaFileHandler
import exploring.JavaFilter
import parsing.Dependencies
import parsing.DependencyFinder
import java.nio.file.Path
import kotlin.io.path.isDirectory

class ProjectDependencyAnalyzer {

    fun analyze(folder: Path): List<Dependencies> {
        checkDirIsKosher(folder)
        val files = listFiles(folder)
            .filter { DependencyFinder(it).getClasses().isNotEmpty() }
        val allClasses = files.flatMap { path -> DependencyFinder(path).getClasses() }

        return files.map { path ->
            DependencyFinder(path).findDependencies(allClasses)
        }
    }

    private fun checkDirIsKosher(file: Path) {
        if (!file.isDirectory()) {
            throw IllegalArgumentException("The file selected is not a folder")
        }
    }

    private fun listFiles(path: Path): ArrayList<Path> {
        val fileHandler = JavaFileHandler()
        DirExplorer(JavaFilter(), fileHandler).explore(path)
        return fileHandler.relevantFiles
    }
}
