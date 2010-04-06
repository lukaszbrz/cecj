package cecj.interaction;

import java.util.ArrayList;
import java.util.List;

import cecj.problems.SymmetricCompetitionProblem;
import ec.EvolutionState;
import ec.Individual;
import ec.util.Parameter;

/**
 * 
 * 
 * @author Marcin Szubert
 * 
 */
public class IntraPopulationInteractionScheme implements InteractionScheme {

	private SymmetricCompetitionProblem problem;

	public void setup(EvolutionState state, Parameter base) {
		if (!(state.evaluator.p_problem instanceof SymmetricCompetitionProblem)) {
			state.output.fatal("Intrapopulation interactions need symmetric problem definition\n");
		} else {
			problem = (SymmetricCompetitionProblem) state.evaluator.p_problem;
		}
	}

	public List<List<InteractionResult>> performInteractions(EvolutionState state, int subpop,
			List<List<Individual>> opponents) {

		List<List<InteractionResult>> subpopulationResults = new ArrayList<List<InteractionResult>>();
		Individual[] competitors = state.population.subpops[subpop].individuals;
		List<Individual> curOpponents = opponents.get(subpop);

		for (Individual competitor : competitors) {
			List<InteractionResult> results = new ArrayList<InteractionResult>();
			for (Individual opponent : curOpponents) {
				results.add(problem.compete(state, competitor, opponent).first);
				results.add(problem.compete(state, opponent, competitor).second);
			}
			subpopulationResults.add(results);
		}

		return subpopulationResults;
	}

	public int getEvaluationsNumber(EvolutionState state, List<List<Individual>> opponents,
			boolean populationOpponents) {
		int result = 0;
		for (int subpop = 0; subpop < state.population.subpops.length; subpop++) {
			int o = opponents.get(subpop).size();
			int n = state.population.subpops[subpop].individuals.length;

			if (populationOpponents) {
				result += (2 * (n - o) * o);
				result += (o * (o - 1));
			} else {
				result += (2 * n * o);
			}
		}

		return result;
	}
}