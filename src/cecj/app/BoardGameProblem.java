package cecj.app;

import ec.EvolutionState;
import ec.Individual;
import ec.util.Parameter;
import games.BoardGame;
import games.player.EvolvedPlayer;
import games.scenario.GameScenario;
import games.scenario.RandomizedTwoPlayersGameScenario;
import games.scenario.TwoPlayerTDLScenario;
import cecj.problem.TestBasedProblem;

public class BoardGameProblem extends TestBasedProblem {

	private static final String P_GAME = "game";
	private static final String P_PLAYER = "player";

	private static final String P_RANDOMNESS = "randomness";
	private static final String P_LEARNING_RATE = "learning-rate";
	private static final String P_BINARY_OUTCOUME = "binary-outcome";
	private static final String P_TERNARY_OUTCOUME = "ternary-outcome";

	private double randomness;
	private boolean randomizedPlay;
	private boolean binaryOutcome;
	private boolean ternaryOutcome;

	private double learningRate;
	private boolean learningPlay;

	private BoardGame boardGame;
	private EvolvedPlayer playerPrototype;

	@Override
	public void setup(EvolutionState state, Parameter base) {
		super.setup(state, base);

		Parameter gameParam = new Parameter(P_GAME);
		boardGame = (BoardGame) state.parameters.getInstanceForParameter(gameParam, null,
				BoardGame.class);

		Parameter playerParam = new Parameter(P_PLAYER);
		playerPrototype = (EvolvedPlayer) state.parameters.getInstanceForParameter(playerParam,
				null, EvolvedPlayer.class);

		Parameter randomnessParam = base.push(P_RANDOMNESS);
		if (state.parameters.exists(randomnessParam)) {
			randomness = state.parameters.getDoubleWithDefault(randomnessParam, null, 0);
			randomizedPlay = true;
		} else {
			randomizedPlay = false;
		}

		Parameter learningRateParam = base.push(P_LEARNING_RATE);
		if (state.parameters.exists(learningRateParam)) {
			learningRate = state.parameters.getDoubleWithDefault(learningRateParam, null, 0.01);
			learningPlay = true;
		} else {
			learningPlay = false;
		}

		Parameter binaryOutcomeParam = base.push(P_BINARY_OUTCOUME);
		binaryOutcome = state.parameters.getBoolean(binaryOutcomeParam, null, false);
		Parameter ternaryOutcomeParam = base.push(P_TERNARY_OUTCOUME);
		ternaryOutcome = state.parameters.getBoolean(ternaryOutcomeParam, null, false);
	}

	@Override
	public int test(EvolutionState state, Individual candidate, Individual test) {
		GameScenario scenario;
		EvolvedPlayer[] players = new EvolvedPlayer[] { playerPrototype.createEmptyCopy(),
				playerPrototype.createEmptyCopy() };
		
		try {
			players[0].readFromIndividual(candidate);
			players[1].readFromIndividual(test);
		} catch (IllegalArgumentException ex) {
			state.output.fatal("Players can not be constructed from this type of individual");
		}
		
		if (learningPlay) {
			scenario = new TwoPlayerTDLScenario(state.random[0], players, randomness, learningRate);
		} else if (randomizedPlay) {
			scenario = new RandomizedTwoPlayersGameScenario(state.random[0], players, new double[] {
					randomness, randomness });
		} else {
			scenario = new RandomizedTwoPlayersGameScenario(state.random[0], players, new double[] {
					0, 0 });
		}

		boardGame.reset();
		if (binaryOutcome) {
			return (scenario.play(boardGame) > 0) ? 1 : 0;
		} else if (ternaryOutcome) {
			int result = scenario.play(boardGame);
			return (result > 0) ? 3 : ((result == 0) ? 1 : 0);
		} else {
			return scenario.play(boardGame);
		}
	}
}
