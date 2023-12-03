plugins {
    kotlin("jvm") version "1.9.20"
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("io.arrow-kt:arrow-core:1.2.1")
    implementation("cc.ekblad.konbini:konbini:0.1.3")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
