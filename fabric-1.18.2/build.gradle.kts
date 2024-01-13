plugins {
    id("java")
    id("fabric-loom") version "1.2-SNAPSHOT"
}

version = project.property("mod_version")!!
group = project.property("maven_group")!!

repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    modImplementation("com.sk89q.worldedit:worldedit-fabric-mc1.18.2:7.2.10")
}

java {
    withSourcesJar()
}

tasks.processResources {
    inputs.property("version", project.property("version"))
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    inputs.property("description", project.property("description"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(mapOf(
                "version" to project.property("version"),
                "minecraft_version" to project.property("minecraft_version"),
                "loader_version" to project.property("loader_version"),
                "description" to project.property("description")
        ))
    }
}

