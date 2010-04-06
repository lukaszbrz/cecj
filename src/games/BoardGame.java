package games;

import java.util.List;

/**
 * Interface describing game rules. Implementing class manages the game state.
 * 
 * @author Marcin Szubert
 * 
 */
public interface BoardGame {

	public boolean endOfGame();

	public double evalMove(Player player, GameMove move);

	public List<? extends GameMove> findMoves();

	public int getCurrentPlayer();

	public double getOutcome();

	public void makeMove(GameMove move);

	public void reset();

	public Board getBoard();

	public void pass();
}