package etf.dotsandboxes.tn160392d;


public class MediumComputer extends MiniMaxPlayer {

	protected int heuristic(final Board board, int color) {
		int value;
		if (referenceColor == Board.RED)
			value = board.getRedScore() - board.getBlueScore();
		else
			value = board.getBlueScore() - board.getRedScore();
		return value;
	}

	
}
