import graphing.PlantUmlGrapher
import java.awt.*

import java.nio.file.Paths
import javax.swing.JFileChooser

private const val SELECT_THE_PROJECT_FOLDER = "Select the project folder"

fun main()  {
    val frame = Frame("Graph a project")

    val button: Component = Button(SELECT_THE_PROJECT_FOLDER)

    frame.add(button, BorderLayout.CENTER);
    val jFileChooser = JFileChooser()
    jFileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    jFileChooser.showDialog(frame, SELECT_THE_PROJECT_FOLDER)

    if(jFileChooser.selectedFile != null) {
        val absPath = jFileChooser.selectedFile.absolutePath
        val dependencies = ProjectDependencyAnalyzer().analyze(Paths.get(absPath))
        val indexUri = PlantUmlGrapher().createGraph(dependencies)
        Desktop.getDesktop().browse(indexUri);
    }
}
