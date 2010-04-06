package cecj.app;

import ec.EvolutionState;
import games.Player;
import games.WPCPlayer;
import games.scenarios.GameScenario;
import games.scenarios.RandomizedTwoPlayersGameScenario;

public abstract class WPCPlayerFitnessCalculator extends GamePlayerFitnessCalculator {

	@Override
	protected GameScenario getScenario(EvolutionState state, double[] player) {
		Player player1 = new WPCPlayer(player);
		Player player2 = getOpponent(player.length);

		return new RandomizedTwoPlayersGameScenario(state.random[0], new Player[] { player1,
				player2 }, new double[] { evaluatedRandomness, evaluatorRandomness });
	}

	@Override
	protected GameScenario getInverseScenario(EvolutionState state, double[] player) {
		Player player1 = new WPCPlayer(player);
		Player player2 = getOpponent(player.length);

		return new RandomizedTwoPlayersGameScenario(state.random[0], new Player[] { player2,
				player1 }, new double[] { evaluatorRandomness, evaluatedRandomness });
	}

	protected abstract WPCPlayer getOpponent(int length);
}