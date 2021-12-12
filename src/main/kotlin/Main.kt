import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Desktop
import java.awt.FileDialog
import java.io.FilenameFilter
import java.net.URI
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
                jFileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY;
                jFileChooser.showDialog(ComposeWindow(), SELECT_THE_PROJECT_FOLDER);

                ProjectDependencyAnalyzer().analyze(jFileChooser.selectedFile.absolutePath)
                //Desktop.getDesktop().browse(URI("http://www.example.com"));
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
