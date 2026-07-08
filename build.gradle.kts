
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.cio.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.cio)
    implementation(ktorLibs.server.core)
    implementation(libs.logback.classic)

    // --- ADDED EXPOSED DEPENDENCIES FROM MAVEN ---
    implementation("org.jetbrains.exposed:exposed-core:1.0.0")
    implementation("org.jetbrains.exposed:exposed-dao:1.0.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.0.0")

    // An embedded local database (H2) so you can test your database code immediately
    implementation("com.h2database:h2:2.2.224")

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)

    implementation("org.jetbrains.exposed:exposed-java-time:1.0.0")

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
