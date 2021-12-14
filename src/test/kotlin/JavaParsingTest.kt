import org.junit.jupiter.api.Test
import java.nio.file.Paths

class JavaParsingTest {

    @Test
    fun shouldParseModel () {
        ProjectDependencyAnalyzer().analyze(Paths.get("/media/lymphocyte/projets/Arca-Sphynx/sphynx-back"))
    }
}