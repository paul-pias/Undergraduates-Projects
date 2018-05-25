import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/*
 * The BoardComponent class creates the GUI component for the max-connect4 game.
 */

public class BoardComponent extends JComponent {
	/**
	 * Number of pixels for a single coordinate
	 */
	public static final int SQUARE_SIZE = 63;


	private Board game;


	public BoardComponent(Board game) {
		this.game = game;
	}

	/**
	 * Draws the components of the game
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.white);
		for (int i = 0; i < Board.BOARD_SIZE; i++) {
			for (int j = 0; j < Board.BOARD_SIZE; j++) {
				if (game.getBoard(true)[i][j] == Human.HUMAN_PLAYER)
					g2.setColor(Color.green);
				  
				else if (game.getBoard(true)[i][j] == AI.AI_PLAYER)
					g2.setColor(Color.red);

				g2.fillOval(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE,
						SQUARE_SIZE);
				g2.setColor(Color.white);
			}
		}

		g2.setColor(Color.black);
		for (int i = 0; i <= Board.BOARD_SIZE; i++) {
			g2.drawLine(0, i * SQUARE_SIZE, Board.BOARD_SIZE * SQUARE_SIZE, i
					* SQUARE_SIZE);
			g2.drawLine(i * SQUARE_SIZE, 0, i * SQUARE_SIZE, Board.BOARD_SIZE
					* SQUARE_SIZE);
		}
	}
}
