package etf.dotsandboxes.tn160392d;

import java.util.ArrayList;

import javax.swing.JTextArea;

public class EasyComputer implements Player {
	
    private JTextArea hWindow = null;

	@Override
	public Line getNextMove(Board board, int color, int depth) {

		ArrayList<Line> moves = board.getAvailableMoves();
		int moveCount = moves.size();
		int nextMove = -1;

		// Check all possible moves to see if some box can be closed
		for (int i = 0; i < moveCount; i++) {
			Board nextBoard = board.getNewBoard(moves.get(i), color);
			if (nextBoard.getBoxCount(4) > board.getBoxCount(4)) {
				// Write to dialog box if step by step mode is enabled
				if(hWindow != null) hWindow.setText("\nClosing the box with move " + Player.convertToOutput(moves.get(i)));
				// Return the move that closes box
				return moves.get(i);
			}
		}
		
		// No closing move, get some random move
		if (nextMove == -1)
			nextMove = (int) (Math.random() * moveCount);
		
		// Write to dialog box if step by step mode is enabled
		if(hWindow != null) hWindow.setText("\nRandom move: " + Player.convertToOutput(moves.get(nextMove)));
		
		return moves.get(nextMove);
	}

	@Override
	public void setTextArea(JTextArea jta) {
		hWindow = jta;
		
	}

}
