import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
}

group = "fr.raluy"
version = "1.0"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(group= "net.sourceforge.plantuml", name= "plantuml", version= "8059")
    implementation("com.github.javaparser:javaparser-core:3.25.3")
    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.3")
    implementation("com.github.javaparser:javaparser-core-serialization:3.25.3")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.test {
    useJUnitPlatform()

    maxHeapSize = "1G"
}