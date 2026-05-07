import org.gradle.jvm.tasks.Jar
plugins {
    idea
    application
    id("java")
}

group = "xyz.pbsi"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:6.1.2")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("commons-cli:commons-cli:1.5.0")
}

tasks.test {
    useJUnitPlatform()
}


application {
    // Tell Gradle what the main class is that it should run, this differs per project based on your packages and classes.
    mainClass.set("xyz.pbsi.Main")
}

// We make a task "farJar", this is a task that will create a jar file that contains all the dependencies.
val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Implementation-Title"] = "HCBot"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "xyz.pbsi.Main" // Same mainclass as the application plugin setting
    }
    from(configurations.runtimeClasspath.get().map{ if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
}

tasks {
    "build" {
        // And we tell during the build task, that it DEPENDS on the fatJar task, so it will always run the fatJar task before the build task.
        dependsOn(fatJar)
    }
}