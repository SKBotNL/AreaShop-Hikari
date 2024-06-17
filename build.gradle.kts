plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "nl.skbotnl.areashop-hikari"
version = "1.1.3"

val apiVersion = "1.19"

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to version,
        "apiVersion" to apiVersion,
    )

    filesMatching("plugin.yml") {
        expand(props)
    }
}


repositories {
    mavenCentral()

    maven {
        url = uri("https://repo.purpurmc.org/snapshots")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.ghostchu:quickshop-api:6.2.0.4")
    compileOnly(files("libs/AreaShop-2.7.17.jar"))
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.10")

    implementation("com.ghostchu.quickshop.compatibility:common:6.2.0.5")
}

tasks.shadowJar {
    minimize()
}

tasks.shadowJar.configure {
    archiveClassifier.set("")
}

tasks.jar {
    dependsOn("shadowJar")
}

tasks.jar.configure {
    archiveClassifier.set("part")
}

kotlin {
    jvmToolchain(17)
}
