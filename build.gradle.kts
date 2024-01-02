plugins {
    kotlin("jvm") version "1.9.20"
}

group = "nl.groen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.6")
    implementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}
