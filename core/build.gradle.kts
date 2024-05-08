version = "1.0.0"

plugins {
    id("java-library")
}

dependencies {
    api(project(":api"))
    maven(mavenCentral(), "com.google.code.gson:gson:2.8.8")
}

labyModProcessor {
    referenceType = net.labymod.gradle.core.processor.ReferenceType.DEFAULT
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}