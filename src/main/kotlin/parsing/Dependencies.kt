package parsing

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.type.Type

class Dependencies(val clazz: ClassOrInterfaceDeclaration, val dependencies: List<Type>) {

}
