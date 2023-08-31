import net.minecraftforge.gradle.userdev.UserDevExtension
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.spongepowered.asm.gradle.plugins.MixinExtension

buildscript {
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    java
    idea
    `maven-publish`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("net.minecraftforge.gradle") version "6.+"
}

apply(plugin = "org.spongepowered.mixin")

val mcVersion: String by project
val forgeVersion: String by project
val modVersion: String by project

val coroutinesVersion: String by project
val serializationVersion: String by project

val shadow: Configuration by configurations.creating

val main = sourceSets["main"]

group = "ru.hollowhorizon"
version = modVersion

evaluationDependsOnChildren()

jarJar.enable()

configurations {
    runtimeElements {
        setExtendsFrom(emptySet())

        artifacts.clear()
        outgoing.artifact(tasks.jarJar)
    }
    minecraftLibrary {
        extendsFrom(shadow)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

configure<UserDevExtension> {
    mappings("official", mcVersion)

    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            workingDirectory (project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "FancyChunks")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")
            arg("-mixin.config=fancychunks.mixins.json")

            mods {
                create("fancychunks") {
                    sources(the<JavaPluginExtension>().sourceSets.getByName("main"))
                }
            }
        }

        create("server") {
            workingDirectory (project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "FancyChunks")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")
            arg("-mixin.config=fancychunks.mixins.json")

            mods {
                create("fancychunks") {
                    sources(the<JavaPluginExtension>().sourceSets.getByName("main"))
                }
            }
        }

        create("gameTestServer") {
            workingDirectory (project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "FancyChunks")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")
            arg("-mixin.config=fancychunks.mixins.json")

            mods {
                create("fancychunks") {
                    sources(the<JavaPluginExtension>().sourceSets.getByName("main"))
                }
            }
        }

        create("data") {
            workingDirectory (project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            args("--mod", "fancychunks", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))

            mods {
                create("fancychunks") {
                    sources(the<JavaPluginExtension>().sourceSets.getByName("main"))
                }
            }
        }
    }
}

sourceSets {
    named("main") {
        resources.srcDir("src/generated/resources")
    }
}

configure<MixinExtension> {
    add(main, "fancychunks.refmap.json")
}

repositories {
    flatDir {
        dir("libs")
    }

    mavenCentral()
}

dependencies {
    minecraft("net.minecraftforge:forge:$mcVersion-$forgeVersion")

    compileOnly(fg.deobf("lib:rubidium:0.5.6"))
    runtimeOnly(fg.deobf("lib:rubidium:0.5.6"))
    compileOnly(fg.deobf("net.coderbot:oculus:mc1.18.2-1.6.4"))
    runtimeOnly(fg.deobf("net.coderbot:oculus:mc1.18.2-1.6.4"))

    shadow("org.jetbrains:annotations:24.0.0")
    shadow(kotlin("stdlib"))
    shadow(kotlin("stdlib-common"))

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks {
    jarJar.configure {
        from(provider { shadow.map(::zipTree).toTypedArray() })
    }

    withType<Jar> {
        from(main.output)
        manifest {
            attributes(
                mapOf(
                    "Specification-Title" to "fancychunks",
                    "Specification-Vendor" to "HollowHorizon",
                    "Specification-Version" to "1",
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to version,
                    "Implementation-Timestamp" to ZonedDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")),
                    "MixinConfigs" to "fancychunks.mixins.json"
                )
            )
        }
        finalizedBy("reobfJar")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
kotlin {
    jvmToolchain(17)
}