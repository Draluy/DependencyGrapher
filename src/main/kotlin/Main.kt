import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import graphing.PlantUmlGrapher
import java.awt.Desktop
import java.nio.file.Paths
import javax.swing.JFileChooser

private const val SELECT_THE_PROJECT_FOLDER = "Select the project folder"

@Composable
@Preview
fun App() {
    val text by remember { mutableStateOf(SELECT_THE_PROJECT_FOLDER) }
    DesktopTheme {
        Row(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(onClick = {
                val jFileChooser = JFileChooser()
                jFileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                jFileChooser.showDialog(ComposeWindow(), SELECT_THE_PROJECT_FOLDER)

                if(jFileChooser.selectedFile != null) {
                    val absPath = jFileChooser.selectedFile.absolutePath
                    val dependencies = ProjectDependencyAnalyzer().analyze(Paths.get(absPath))
                    val indexUri = PlantUmlGrapher().createGraph(dependencies)
                    Desktop.getDesktop().browse(indexUri);
                }
            }) {
                Text(text)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
