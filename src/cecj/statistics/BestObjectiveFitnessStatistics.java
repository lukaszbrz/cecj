package cecj.statistics;

import java.io.File;
import java.io.IOException;

import ec.EvolutionState;
import ec.Individual;
import ec.Statistics;
import ec.util.Output;
import ec.util.Parameter;

public class BestObjectiveFitnessStatistics extends Statistics {

	private static final String P_FREQUENCY = "frequency";
	private static final String P_FITNESS_CALCULATOR = "fitness-calc";
	private static final String P_FITNESS_FILE = "fitness-file";
	private static final String P_IND_FILE = "ind-file";

	public int fitnessStatisticsLog;
	public int individualsLog;

	public BestObjectiveFitnessStatistics() {
		fitnessStatisticsLog = 0;
		individualsLog = 0;
	}

	private int frequency;
	private ObjectiveFitnessCalculator fitnessCalc;

	@Override
	public void setup(EvolutionState state, Parameter base) {
		super.setup(state, base);

		Parameter fitnessCalcParameter = base.push(P_FITNESS_CALCULATOR);
		fitnessCalc = (ObjectiveFitnessCalculator) state.parameters.getInstanceForParameter(
				fitnessCalcParameter, null, ObjectiveFitnessCalculator.class);
		fitnessCalc.setup(state, fitnessCalcParameter);

		Parameter frequencyParam = base.push(P_FREQUENCY);
		frequency = state.parameters.getIntWithDefault(frequencyParam, null, 1);

		File fitnessStatisticsFile = state.parameters.getFile(base.push(P_FITNESS_FILE), null);
		if (fitnessStatisticsFile != null) {
			try {
				fitnessStatisticsLog = state.output.addLog(fitnessStatisticsFile, Output.V_VERBOSE,
						false, true, false);
			} catch (IOException i) {
				state.output.fatal("An IOException occurred while trying to create the log "
						+ fitnessStatisticsFile + ":\n" + i);
			}
		}

		File individualsFile = state.parameters.getFile(base.push(P_IND_FILE), null);
		if (individualsFile != null) {
			try {
				individualsLog = state.output.addLog(individualsFile, Output.V_VERBOSE, false,
						true, false);
			} catch (IOException i) {
				state.output.fatal("An IOException occurred while trying to create the log "
						+ individualsFile + ":\n" + i);
			}
		}
	}

	@Override
	public void postEvaluationStatistics(EvolutionState state) {
		super.postEvaluationStatistics(state);

		if ((state.generation % frequency != 0) && (state.generation != state.numGenerations - 1)) {
			return;
		}

		//TODO - only first population is evaluated
		for (int subpop = 0; subpop < 1 /* state.population.subpops.length */; ++subpop) {
			Individual maxSubjectiveFitnessInd = state.population.subpops[subpop].individuals[0];
			float maxSubjectiveFitness = maxSubjectiveFitnessInd.fitness.fitness();

			for (int i = 1; i < state.population.subpops[subpop].individuals.length; ++i) {
				Individual ind = state.population.subpops[subpop].individuals[i];
				if (ind.fitness.fitness() > maxSubjectiveFitness) {
					maxSubjectiveFitness = ind.fitness.fitness();
					maxSubjectiveFitnessInd = ind;
				}
			}

			float objectiveFitnessOfSubjectivelyBest = fitnessCalc.calculateObjectiveFitness(state,
					maxSubjectiveFitnessInd);

			state.output.println(state.generation + "\t" + objectiveFitnessOfSubjectivelyBest,
					Output.V_VERBOSE + 1, fitnessStatisticsLog);
			state.output.println("Generation: " + state.generation, Output.V_VERBOSE + 1,
					individualsLog);
			maxSubjectiveFitnessInd.printIndividual(state, individualsLog, Output.V_VERBOSE + 1);
		}

		if (state.generation == state.numGenerations - 1) {
			saveIndividuals(state);
		}
	}

	private void saveIndividuals(EvolutionState state) {
		for (int subpop = 0; subpop < state.population.subpops.length; ++subpop) {
			state.output.println("\nSubpopulation " + subpop + " individuals : ",
					Output.V_VERBOSE + 1, fitnessStatisticsLog);
			Individual[] inds = state.population.subpops[subpop].individuals;
			for (Individual ind : inds) {
				ind.printIndividual(state, fitnessStatisticsLog, Output.V_VERBOSE + 1);
			}
		}
	}
}
