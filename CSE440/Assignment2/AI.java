import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The AI class represents an AI player in the game implements the algorithms
 * that calculate the optimal move for the computer.
 *
 */
public class AI extends Player {
	/**
	 * Value indicating an AI player
	 */
	public static final int AI_PLAYER = -2;

	/**
	 * Constants used for minimax algorithm calculations
	 */
	private static final int BRANCHING_FACTOR = 4;
	private static final int LOOK_AHEAD = 9;

	private Board game;

	/**
	 * Constructs an AI player for the game
	 */
	public AI(Board game) {
		this.game = game;
	}

	/**
	 * Gets the possible legal moves, calls the minimax function on the best
	 * candidates, and moves the AI player
	 */
	public void move() {
		ArrayList<Point> moves = getPossibleMoves(game);

		int max = Integer.MIN_VALUE;
		Point bestMove = null;
		for (int i = 0; i < Math.min(moves.size(), BRANCHING_FACTOR); i++) {
			int value = minimax(game, moves.get(i), LOOK_AHEAD,
					Integer.MIN_VALUE, Integer.MAX_VALUE, true);

			if (value > max) {
				max = value;
				bestMove = moves.get(i);
			}
		}

				super.move(game, AI_PLAYER, bestMove.x, bestMove.y);
	}

	/**
	 * The recursive function that uses minimax with alpha-beta pruning and
	 * heuristic search to estimate the optimal move in the game tree
	 * 
	 * @param game
	 *            the connect-five game
	 * @param move
	 *            the move of interest
	 * @param depth
	 *            the depth level of the search tree
	 * @param alpha
	 *            the maximum value that the maximizing player is assured of
	 * @param beta
	 *            the minimum value that the minimizing player is assured of
	 * @param max
	 *            true if the move is for the maximizing player, false otherwise
	 * @return the heuristic value of the move
	 */
	private int minimax(Board game, Point move, int depth, int alpha, int beta,
			boolean max) {
		// return the heuristic value if the node is a terminal node
		if (depth == 0)
			return game.getBoardValue();
		else if (game.getWinner() != 0)
			return -depth * game.getBoardValue();

		Board copy = game.clone();
		super.move(copy, max ? AI_PLAYER : Human.HUMAN_PLAYER, move.x, move.y);

		ArrayList<Point> moves = getPossibleMoves(game);
		if (max) {
			int bestValue = Integer.MIN_VALUE;
			for (int i = 0; i < Math.min(moves.size(), BRANCHING_FACTOR); i++) {
				bestValue = Math.max(
						bestValue,
						minimax(copy, moves.get(i), depth - 1, alpha, beta,
								false));

				alpha = Math.max(alpha, bestValue);
				if (beta <= alpha) // beta cut-off
					break;
			}
			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			for (int i = 0; i < Math.min(moves.size(), BRANCHING_FACTOR); i++) {
				bestValue = Math.min(
						bestValue,
						minimax(copy, moves.get(i), depth - 1, alpha, beta,
								true));

				beta = Math.min(beta, bestValue);
				if (beta <= alpha) // alpha cut-off
					break;
			}
			return bestValue;
		}
	}

	/**
	 * Gets the possible legal move of the AI player
	 * @return a list of possible moves, sorted in descending order of their
	 *         initial heuristic value
	 */
	private ArrayList<Point> getPossibleMoves(Board game) {
		ArrayList<Point3D> moves = new ArrayList<>();
		int[][] human = game.getBoard(true);
		int[][] ai = game.getBoard(false);

		for (int i = 0; i < Board.BOARD_SIZE; i++)
			for (int j = 0; j < Board.BOARD_SIZE; j++)
				if (human[i][j] >= 0)
					moves.add(new Point3D(i, j, human[i][j] + ai[i][j]));

		Collections.sort(moves);
		return new ArrayList<Point>(moves);
	}

	/**
	 * Inner class that uses the z-coordinate to store the heuristic value of
	 * the move
	 */
	private class Point3D extends Point implements Comparable<Point3D> {
		
		private int z;

		private Point3D(int x, int y, int z) {
			super(x, y);
			this.z = z;
		}

		@Override
		public int compareTo(Point3D pt) {
			return pt.z - z;
		}
	}
}