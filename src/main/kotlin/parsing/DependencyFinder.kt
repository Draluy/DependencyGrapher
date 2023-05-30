package parsing

import com.github.javaparser.ParserConfiguration
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.type.Type
import java.nio.file.Path


class DependencyFinder (filePath: Path){

    val compilationUnit : CompilationUnit
    val relevantAnnotations = listOf("Autowired", "Inject")

    init {
        StaticJavaParser.setConfiguration(
            ParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
        )
        compilationUnit = StaticJavaParser.parse(filePath)
    }

    fun getClasses(): List<TypeDeclaration<*>> {
        val classes =  compilationUnit.findAll(ClassOrInterfaceDeclaration::class.java)

        return classes
    }

    fun findDependencies(allClasses: List<TypeDeclaration<*>>): Dependencies {
        val constructors = getRelevantConstructors()
        val fields = getRelevantFields()


        val dependencies = getPossibleDependenciesFromConstructors(constructors, allClasses)
        val fieldDependencies = getPossibleDependenciesFromFields(fields, allClasses)

        val thisClassDependencies = dependencies + fieldDependencies

        val classes = getClasses()
        println("class ${classes.map { c -> c.nameAsString }} , found ${thisClassDependencies.map{ cd -> cd.asString()}}")
        return Dependencies(classes.get(0), thisClassDependencies)
    }

    private fun getRelevantConstructors(): List<ConstructorDeclaration> {
        return compilationUnit.findAll(ConstructorDeclaration::class.java)
    }

    private fun getRelevantFields(): List<FieldDeclaration> {
        val fields: List<FieldDeclaration> = compilationUnit.findAll(FieldDeclaration::class.java)
            .filter { field ->
                val annotations = field.annotations.map { annotation -> annotation.nameAsString }
                relevantAnnotations.any { ra -> annotations.contains(ra) }
            }
        return fields
    }

    private fun getPossibleDependenciesFromConstructors(
        constructors: List<ConstructorDeclaration>,
        allClasses: List<TypeDeclaration<*>>
    ): List<Type> {
        return constructors.flatMap { constructor ->
            constructor.parameters.filter { parameter ->
                allClasses.any { detectedClass ->
                    detectedClass.fullyQualifiedName.get() == getFullyQualifiedName(parameter.type)
                }
            }.map { param -> param.type }
        }
    }

    private fun getPossibleDependenciesFromFields(
        fields: List<FieldDeclaration>,
        allClasses: List<TypeDeclaration<*>>
    ): List<Type> {
        return fields.filter { field ->
            allClasses.any { detectedClass ->
                detectedClass.fullyQualifiedName.get() == getFullyQualifiedName(field.elementType)
            }
        }.map { param -> param.commonType }
    }

    private fun getFullyQualifiedName(elementType: Type): String {
        val importFound : ImportDeclaration? = compilationUnit.imports.find { import -> import.nameAsString.endsWith(elementType.asString()) }

        if (importFound != null) {
            return importFound.nameAsString
        }

        var node : Node? = elementType.parentNode.orElse(null)

        while (node !is CompilationUnit){
            node = node?.parentNode?.get()
        }

        return (node).packageDeclaration.get().name.asString()+ '.' + elementType
    }

}