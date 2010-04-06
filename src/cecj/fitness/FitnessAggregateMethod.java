/*
  Copyright 2009 by Marcin Szubert
  Licensed under the Academic Free License version 3.0
*/

package cecj.fitness;

import java.util.List;

import cecj.interaction.InteractionResult;

import ec.EvolutionState;

/**
 * A method of aggregating individuals' interactions results.
 * 
 * @author Marcin Szubert
 * 
 */
public interface FitnessAggregateMethod {

	public void prepareToAggregate(EvolutionState state, int subpop);

	public void addToAggregate(EvolutionState state, int subpop,
			List<List<InteractionResult>> subpopulationResults, int weight);

	/**
	 * Assigns fitness to all individuals in the population according to their interaction outcomes.
	 */
	public void assignFitness(EvolutionState state, int subpop);
}