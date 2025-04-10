import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java plugin
    id("java-library")

    // Fairy plugin
    id("io.fairyproject")

    // Dependency management plugin
    id("io.spring.dependency-management") version "1.1.0"

    //Kotlin plugin
    id("org.jetbrains.kotlin.jvm") version "1.9.23" apply false

    //Shadow plugin, provides the ability to shade fairy and other dependencies to compiled jar
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = properties("group")
version = properties("version")

// Fairy configuration
fairy {
    name.set(properties("name"))
    // Main Package
    mainPackage.set(properties("package"))
    // Fairy Package
    fairyPackage.set("io.fairyproject")

    bukkitProperties().bukkitApi = "1.13"
    bukkitProperties().foliaSupported = true
    bukkitProperties().description = "Fairy framework in library form"
    bukkitProperties().loadBefore += "CoordinateOffset"
}

runServer {
    version.set(properties("spigot.version"))
}

dependencies {
    runtimeOnly("io.fairyproject:bukkit-bootstrap")
    api("io.fairyproject:bukkit-platform")
    api("io.fairyproject:mc-animation")
    api("io.fairyproject:bukkit-command")
    api("io.fairyproject:bukkit-gui")
    api("io.fairyproject:mc-hologram")
    api("io.fairyproject:core-config")
    api("io.fairyproject:bukkit-xseries")
    api("io.fairyproject:bukkit-items")
    api("io.fairyproject:mc-nametag")
    api("io.fairyproject:mc-sidebar")
    api("io.fairyproject:bukkit-visibility")
    api("io.fairyproject:bukkit-visual")
    api("io.fairyproject:bukkit-timer")
    api("io.fairyproject:bukkit-nbt")
    api("io.fairyproject:mc-tablist")
}

// Repositories
repositories {
    mavenCentral()
    maven(url = uri("https://oss.sonatype.org/content/repositories/snapshots/"))
    maven(url = uri("https://repo.codemc.io/repository/maven-public/"))
    // Spigot's repository for spigot api dependency
    maven(url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"))
    maven(url = uri("https://repo.imanity.dev/imanity-libraries"))
}

// Dependencies
dependencies {
    // Spigot dependency
    compileOnly("org.spigotmc:spigot-api:${properties("spigot.version")}-R0.1-SNAPSHOT")
}

tasks.withType(ShadowJar::class.java) {
    // Relocate fairy to avoid plugin conflict
    relocate("net.kyori", "io.fairyproject.libs.kyori")
    relocate("com.cryptomorin.xseries", "io.fairyproject.libs.xseries")
    relocate("org.yaml.snakeyaml", "io.fairyproject.libs.snakeyaml")
    relocate("com.google.gson", "io.fairyproject.libs.gson")
    relocate("com.github.retrooper.packetevents", "io.fairyproject.libs.packetevents")
    relocate("io.github.retrooper.packetevents", "io.fairyproject.libs.packetevents")
    relocate("de.tr7zw.changeme.nbtapi", "io.fairyproject.libs.nbtapi")
    relocate("io.fairyproject.bootstrap", "io.fairyproject.library.bootstrap")

    archiveFileName.set("fairy-lib-plugin.jar")
}