plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = parent?.group!!
version = parent?.version!!

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.luben:zstd-jni:1.5.4-1")
    implementation("com.github.marianobarrios:tls-channel:0.7.0")
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
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