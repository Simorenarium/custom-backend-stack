plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    application
}

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines:0.19.2")
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // Configuration
    implementation("com.sksamuel.hoplite:hoplite-core:2.1.0")
    implementation("com.sksamuel.hoplite:hoplite-yaml:2.1.0")

    // HTTP/Rest
    implementation("com.sparkjava:spark-kotlin:1.0.0-alpha")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.37.3")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")
    implementation("org.flywaydb:flyway-core:8.5.7")
    implementation("org.postgresql:postgresql:42.3.3")
    runtimeOnly("com.h2database:h2:2.1.212")
}

application {
    // Define the main class for the application.
    mainClass.set("coffee.michel.usermanager.AppKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
