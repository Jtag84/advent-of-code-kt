plugins {
    kotlin("jvm") version "1.9.21"
    application
}

repositories {
    mavenCentral()
    google()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("cc.ekblad.konbini:konbini:0.1.3")
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("io.arrow-kt:arrow-core:1.2.1")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
