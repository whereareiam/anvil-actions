// The project the checks run through the actions: one Paper server that one player joins.
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://registry.whereareiam.me/maven/packages")
        maven("https://repo.opencollab.dev/main/")
        maven("https://repo.opencollab.dev/maven-snapshots/")
    }
}

val anvilVersion = providers.environmentVariable("ANVIL_VERSION")
    .orElse(providers.gradleProperty("anvilVersion"))
    .get()

pluginManagement.resolutionStrategy.eachPlugin {
    if (requested.id.id == "me.whereareiam.anvil" || requested.id.id.startsWith("me.whereareiam.anvil."))
        useVersion(anvilVersion)
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("anvil", anvilVersion)
            version("junit", "6.1.3")
            library("anvil-protocol-mcprotocol", "me.whereareiam.anvil", "protocol-mcprotocol").versionRef("anvil")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-platform", "org.junit.platform", "junit-platform-launcher").versionRef("junit")
        }
    }

    repositories {
        mavenCentral()
        maven("https://registry.whereareiam.me/maven/packages")
        maven("https://repo.opencollab.dev/main/")
        maven("https://repo.opencollab.dev/maven-snapshots/")
    }
}

rootProject.name = "anvil-actions-check"
