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
    implementation("com.breinify:brein-time-utilities:1.8.0")
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("commons-io:commons-io:2.15.1")
    implementation("guru.nidi:graphviz-kotlin:0.18.0")
    implementation("io.arrow-kt:arrow-core:1.2.1")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.ejml:ejml-simple:0.43.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    implementation("org.jgrapht:jgrapht-io:1.5.2")
    implementation("org.jgrapht:jgrapht-ext:1.5.2")
}

application {
    mainClass.set("AppKt")
    application.applicationName = "aoc-kt"
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
