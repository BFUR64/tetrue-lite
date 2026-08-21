plugins {
    application
    id("eclipse")
    id("com.gradleup.shadow") version "9.3.1"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("io.github.bfur64:menu-manager:0.9.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "com.teic.trueris.App"
}
