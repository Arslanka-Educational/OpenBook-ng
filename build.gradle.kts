plugins {
    kotlin("jvm") version "1.8.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    // Общие зависимости для всех подпроектов
    dependencies {
        "implementation"(kotlin("stdlib"))
    }
}
