import java.lang.IllegalArgumentException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory

class ProjectDependencyAnalyzer {
    fun analyze(file: String?) {
        if (file == null ) {
            throw IllegalArgumentException("Folder selected is null");
        }
        val dirPath = Paths.get(file)
        if (!dirPath.isDirectory() ) {
            throw IllegalArgumentException("The file selected is not a folder");
        }
        listFiles(dirPath)
    }

    fun listFiles (pathName: Path) {
     //DirEpl
    }
}
