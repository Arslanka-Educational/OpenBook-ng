plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "OpenBook-ng"

include("core-booking")
include("core-registry")
include("core-catalog")
include("gateway")
