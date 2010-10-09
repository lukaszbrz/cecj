package games.player;

import ec.Individual;
import ec.simple.SimpleFitness;
import ec.util.MersenneTwisterFast;
import ec.vector.DoubleVectorIndividual;
import ec.vector.VectorIndividual;
import games.Board;

public class WPCPlayer implements EvolvedPlayer {

	private int boardSize;

	private double[] wpc;

	public WPCPlayer(int boardSize) {
		this.boardSize = boardSize;
		this.wpc = new double[boardSize * boardSize];
	}

	public WPCPlayer(double[] wpc) {
		this.boardSize = (int) Math.sqrt(wpc.length);
		this.wpc = wpc;
	}

	public double getValue(int row, int col) {
		return wpc[(row - 1) * boardSize + (col - 1)];
	}

	public double[] getWPC() {
		return wpc;
	}

	public void setValue(int row, int col, double value) {
		wpc[(row - 1) * boardSize + (col - 1)] = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				builder.append(wpc[i * boardSize + j] + "\t");
			}
			builder.append("\n");
		}
		String res = builder.toString();
		return res;
	}

	public void randomize(MersenneTwisterFast random, double range) {
		for (int i = 0; i < wpc.length; i++) {
			wpc[i] = random.nextDouble() * range;
			if (random.nextBoolean()) {
				wpc[i] *= -1;
			}
		}
	}

	public void TDLUpdate(Board previous, double delta, double[][] traces, double lambda) {
		double evalBefore = Math.tanh(evaluate(previous));
		double derivative = (1 - (evalBefore * evalBefore));

		for (int row = 1; row <= boardSize; row++) {
			for (int col = 1; col <= boardSize; col++) {
				traces[row][col] = traces[row][col] * lambda
						+ (derivative * previous.getValueAt(row, col));
				setValue(row, col, getValue(row, col) + (delta * traces[row][col]));
			}
		}
	}

	public void TDLUpdate(Board previous, double delta) {
		for (int row = 1; row <= boardSize; row++) {
			for (int col = 1; col <= boardSize; col++) {
				setValue(row, col, getValue(row, col) + (delta * previous.getValueAt(row, col)));
			}
		}
	}

	public double evaluate(Board board) {
		double result = 0;
		for (int row = 1; row <= board.getSize(); row++) {
			for (int col = 1; col <= board.getSize(); col++) {
				result += board.getValueAt(row, col) * getValue(row, col);
			}
		}
		return result;
	}

	public void readFromIndividual(Individual ind) {
		if (ind instanceof DoubleVectorIndividual) {
			wpc = ((DoubleVectorIndividual) ind).genome;
		} else {
			throw new IllegalArgumentException("Individual should be of type DoubleVectorIndividual");
		}
	}

	public void writeToIndividual(Individual ind) {
		((VectorIndividual)ind).setGenome(wpc);
		ind.fitness = new SimpleFitness();
	}

	public EvolvedPlayer createEmptyCopy() {
		return new WPCPlayer(boardSize);
	}
}
