plugins {
    application
    id("eclipse")
    id("com.gradleup.shadow") version "9.3.1"
    id("com.github.spotbugs") version "6.5.11"
}

spotbugs {
    toolVersion = "4.10.4"
    ignoreFailures = false
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("io.github.bfur64:menu-manager:0.10.0")
    implementation("io.github.bfur64:micro-sound:0.2.0")
    implementation("org.apache.logging.log4j:log4j-core:2.26.0")
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.4")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "io.github.bfur64.tetrue.App"
}

val projectName = project.name

val installDir = layout.buildDirectory.dir("install/$projectName")
val inputDir = installDir.get().asFile.resolve("lib")

val runtimeDir = layout.buildDirectory.dir("runtime").get().asFile
val packageDir = layout.buildDirectory.dir("jpackage").get().asFile

val mainClass = application.mainClass.get()

val iconPath = layout.projectDirectory
    .file("src/main/packaging/logo.ico")
    .asFile
    .absolutePath

tasks.register<Delete>("cleanRuntime") {
    delete(runtimeDir)
}

tasks.register<Exec>("createRuntime") {
    dependsOn(tasks.installDist, "cleanRuntime")

    // To get the modules, run:
    // jdeps `
    //     --print-module-deps `
    //     --ignore-missing-deps `
    //     --multi-release 21 `
    //     --class-path "..\install\app\lib\*" `
    //     (Get-ChildItem "..\install\app\lib\*.jar").FullName
    // On app/build/libs

    commandLine(
        "jlink",
        "--module-path", "${System.getProperty("java.home")}/jmods",
        "--add-modules", "java.base,java.desktop,java.management,jdk.httpserver",
        "--strip-debug",
        "--no-man-pages",
        "--no-header-files",
        "--output", runtimeDir.absolutePath
    )
}

tasks.register<Delete>("cleanPackage") {
    delete(packageDir)
}

tasks.register<Exec>("packageWindows") {
    dependsOn("createRuntime", "build", "cleanPackage")

    commandLine(
        "jpackage",
        "--type", "app-image",
        "--name", "Tetrue",
        "--input", inputDir.absolutePath,
        "--main-jar", "$projectName.jar",
        "--main-class", mainClass,
        "--dest", packageDir.absolutePath,
        "--runtime-image", runtimeDir.absolutePath,
        "--win-console",
        "--icon", iconPath
    )
}