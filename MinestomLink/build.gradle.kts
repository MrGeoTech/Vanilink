plugins {
    kotlin("jvm") version "1.8.0"
}

group = parent?.group!!
version = parent?.version!!

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.Minestom:Minestom:067227421f")
    implementation("com.github.luben:zstd-jni:1.5.4-1")
    implementation("com.github.Karan-Gandhi:Java-Networking-Library:v1.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}