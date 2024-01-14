plugins {
    id("java")
    id("application")
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

    implementation(project(":common"))
}

java {
    withSourcesJar()
}

tasks.processResources {
    inputs.property("version", project.property("version"))
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    inputs.property("description", project.property("mod_description"))
    inputs.property("mod_id", project.property("mod_id"))
    inputs.property("name", project.property("archives_base_name"))
    inputs.property("author", project.property("author"))
    inputs.property("repo", project.property("repo"))
    inputs.property("issues", project.property("issues"))
    inputs.property("license", project.property("license"))
    inputs.property("worldedit_version", project.property("worldedit_version"))

    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(mapOf(
                "version" to project.property("version"),
                "minecraft_version" to project.property("minecraft_version"),
                "loader_version" to project.property("loader_version"),
                "description" to project.property("mod_description"),
                "mod_id" to project.property("mod_id"),
                "name" to project.property("archives_base_name"),
                "author" to project.property("author"),
                "repo" to project.property("repo"),
                "issues" to project.property("issues"),
                "license" to project.property("license"),
                "worldedit_version" to project.property("worldedit_version")
        ))
    }
}

tasks.jar {
    from(project(":common").sourceSets["main"].output)
}

tasks.remapJar {
    archiveBaseName.set("mcpixelart-fabric")
}

tasks.remapSourcesJar {
    archiveBaseName.set("mcpixelart-fabric")
}