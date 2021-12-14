package graphing

import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import parsing.Dependencies
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths


private const val GRAPH_SVG = "graph.svg"

private const val INDEX_HTML = "index.html"

class PlantUmlGrapher {
    fun createGraph(dependencies: List<Dependencies>): URI {

        val source: String = generateSource(dependencies)
        val reader = SourceStringReader(source)
        val os = ByteArrayOutputStream()
        reader.generateImage(os, FileFormatOption(FileFormat.SVG))
        os.close()

        deleteFiles()
        writeImage(os)
        writeHtml()

        return Paths.get(INDEX_HTML).toUri()
    }

    private fun deleteFiles() {
        Files.deleteIfExists(Paths.get(GRAPH_SVG))
        Files.deleteIfExists(Paths.get(INDEX_HTML))
    }

    private fun writeHtml() {
        val fout = FileOutputStream(INDEX_HTML, true)
        val writer = fout.writer(Charsets.UTF_8)
        writer.append(
            """
                <!doctype html>
                
                <html lang="en">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                
                  <title>A Basic HTML5 Template</title>
                  <meta name="description" content="A simple HTML5 Template for new projects.">
                  <meta name="author" content="SitePoint">
                
                  <meta property="og:title" content="A Basic HTML5 Template">
                  <meta property="og:type" content="website">
                  <meta property="og:url" content="https://www.sitepoint.com/a-basic-html5-template/">
                  <meta property="og:description" content="A simple HTML5 Template for new projects.">
                  <meta property="og:image" content="image.png">
                
                  <link rel="icon" href="/favicon.ico">
                  <link rel="icon" href="/favicon.svg" type="image/svg+xml">
                  <link rel="apple-touch-icon" href="/apple-touch-icon.png">
                
                  <link rel="stylesheet" href="css/styles.css?v=1.0">
                
                </head>
                
                <body>
                <H1>graph</H1>
                <img src = "$GRAPH_SVG" alt="My Happy SVG"/>
                
                </body>
                </html>
            """
        )
        writer.close()
        fout.close()

    }

    private fun generateSource(dependencies: List<Dependencies>): String {
        val depsAsString = dependencies.flatMap { cl ->
            cl.dependencies.map { dep -> "${dep.asString()} --|> ${cl.clazz.nameAsString}" }
        }.joinToString("\n") { it }
        return "@startuml \n $depsAsString \n @enduml"
    }

    private fun writeImage(os: ByteArrayOutputStream) {
        val fout = FileOutputStream(GRAPH_SVG, false)
        fout.write(os.toByteArray())
        fout.close()
    }
}