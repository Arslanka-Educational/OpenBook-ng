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
        "implementation"("org.springframework.boot:spring-boot-starter-validation:3.3.2")
        "implementation"("org.springframework.boot:spring-boot-starter-web:3.3.2")
    }
}
