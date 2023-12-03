plugins {
    kotlin("jvm") version "1.9.20"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("io.arrow-kt:arrow-core:1.2.1")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
