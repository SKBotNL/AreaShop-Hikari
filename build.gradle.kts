plugins {
    id("com.gradleup.shadow") version "8.3.2" // Import shadow API.
    java // Tell gradle this is a java project.
    eclipse // Import eclipse plugin for IDE integration.
    kotlin("jvm") version "2.0.21" // Import kotlin jvm plugin for kotlin/java integration.
}

java {
    // Declare java version.
    sourceCompatibility = JavaVersion.VERSION_17
}

group = "nl.skbotnl.areashop-hikari" // Declare bundle identifier.
version = "1.1.4" // Declare plugin version (will be in .jar).
val apiVersion = "1.19" // Declare minecraft server target version.

tasks.named<ProcessResources>("processResources") {
    val props = mapOf(
        "version" to version,
        "apiVersion" to apiVersion
    )

    inputs.properties(props) // Indicates to rerun if version changes.

    filesMatching("plugin.yml") {
        expand(props)
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri("https://repo.purpurmc.org/snapshots")
    }
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
    compileOnly("com.ghostchu:quickshop-api:6.2.0.6")
    compileOnly(files("libs/AreaShop-2.9.1.jar"))
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.8")
    implementation("com.ghostchu.quickshop.compatibility:common:5.2.0.7")
}

tasks.withType<AbstractArchiveTask>().configureEach { // Ensure reproducible builds.
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.shadowJar {
    archiveClassifier.set("") // Use empty string instead of null
    from("LICENSE") {
        into("/")
    }
    minimize()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.jar {
    archiveClassifier.set("part")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Xlint:deprecation") // Triggers deprecation warning messages.
    options.encoding = "UTF-8"
    options.isFork = true
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.GRAAL_VM
    }
}
