package etf.dotsandboxes.tn160392d;

public class HardComputer extends MiniMaxPlayer {

	protected int heuristic(final Board board, int color) {
		int value = 0;
		
		if (referenceColor == Board.RED)
			value = 20 * board.getRedScore() - 20 * board.getBlueScore();
		else
			value = 20 * board.getBlueScore() - 20 * board.getRedScore();
		
		
		if (referenceColor == color)
			value += 10 * board.getBoxCount(3) - 2 * board.getBoxCount(2);
		else
			value -= 10 * board.getBoxCount(3) - 2 * board.getBoxCount(2);
		
		// If it is end, add bonus points
		if (board.isComplete()) {
			if (board.getWinner() == referenceColor) {
				value += 1000;
			} else {
				value -= 1000;
			}
		}
		
		return value;
	}

}
