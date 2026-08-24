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
    implementation("io.github.bfur64:menu-manager:0.9.2")
    implementation("io.github.bfur64:micro-sound:0.1.1")
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.4")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "com.teic.trueris.App"
}
