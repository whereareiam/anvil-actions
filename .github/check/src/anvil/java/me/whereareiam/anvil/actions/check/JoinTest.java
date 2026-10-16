package me.whereareiam.anvil.actions.check;

import me.whereareiam.anvil.api.player.SimulatedPlayer;
import me.whereareiam.anvil.api.scenario.ScenarioContext;
import me.whereareiam.anvil.capability.server.Server;
import me.whereareiam.anvil.capability.session.Session;
import me.whereareiam.anvil.integration.junit.AnvilTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JoinTest {
	@Test
	@AnvilTest(PaperScenario.class)
	void aliceJoinsPaper(ScenarioContext anvil) {
		SimulatedPlayer alice = anvil.players().create("Alice");
		Session session = alice.capability(Session.class);
		session.connect();
		session.connected();

		assertEquals(PaperScenario.SERVER, alice.capability(Server.class).joined(PaperScenario.SERVER).getRoute().getServer());
		alice.destroy();
	}
}
