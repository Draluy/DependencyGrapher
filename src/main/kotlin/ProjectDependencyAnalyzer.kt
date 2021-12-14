import com.github.javaparser.StaticJavaParser
import com.github.javaparser.symbolsolver.JavaSymbolSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver
import exploring.DirExplorer
import exploring.JavaFileHandler
import exploring.JavaFilter
import parsing.Dependencies
import parsing.DependencyFinder
import java.lang.IllegalArgumentException
import java.nio.file.Path
import kotlin.io.path.isDirectory

class ProjectDependencyAnalyzer {

    init{
        val combinedTypeSolver = CombinedTypeSolver()
        combinedTypeSolver.add(ReflectionTypeSolver())
        val symbolSolver = JavaSymbolSolver(combinedTypeSolver)
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver)
    }

    fun analyze(folder: Path): List<Dependencies> {
        checkDirIsKosher(folder)
        val files = listFiles(folder)
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
