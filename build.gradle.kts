plugins {
    kotlin("jvm") version "2.0.10"
    id("org.jetbrains.dokka") version "1.9.0"
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.8"
}

group = "ie.setu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.thoughtworks.xstream:xstream:1.4.18")
    implementation("org.codehaus.jettison:jettison:1.4.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(16)
}
