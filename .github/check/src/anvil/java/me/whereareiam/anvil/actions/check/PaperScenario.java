package me.whereareiam.anvil.actions.check;

import me.whereareiam.anvil.api.model.process.Distribution;
import me.whereareiam.anvil.api.model.process.MinecraftServer;
import me.whereareiam.anvil.api.model.scenario.AnvilScenario;
import me.whereareiam.anvil.api.scenario.AnvilScenarioDefinition;
import me.whereareiam.anvil.api.type.Platforms;
import org.jetbrains.annotations.NotNull;

/**
 * One Paper server, enough for Anvil to download a distribution, patch it and keep a workspace snapshot.
 */
public final class PaperScenario implements AnvilScenarioDefinition {
	static final String SERVER = "paper";

	@Override
	public @NotNull AnvilScenario define() {
		MinecraftServer paper = MinecraftServer.builder()
				.name(SERVER)
				.platform(Platforms.PAPER)
				.distribution(Distribution.remote("1.21.11", "132"))
				.memoryMegabytes(768)
				.build();

		return AnvilScenario.builder()
				.name("anvil-actions-check")
				.entrypoint(paper.getName())
				.server(paper)
				.build();
	}
}
