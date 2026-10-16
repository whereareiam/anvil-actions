plugins {
    java
    id("me.whereareiam.anvil")
    id("me.whereareiam.anvil.junit")
    id("me.whereareiam.anvil.capability.default")
    id("me.whereareiam.anvil.platform.paper")
}

dependencies {
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly(libs.junit.platform)

    add("anvilRuntimeOnly", libs.anvil.protocol.mcprotocol)
}

anvil {
    acceptEula()
    engine {
        protocolLibrary("mcprotocol")
    }
}
