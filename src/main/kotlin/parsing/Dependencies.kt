package parsing

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.type.Type

class Dependencies(val clazz: TypeDeclaration<*>, val dependencies: List<Type>) {

}
