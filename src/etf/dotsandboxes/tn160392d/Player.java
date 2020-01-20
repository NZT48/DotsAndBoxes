package etf.dotsandboxes.tn160392d;

import javax.swing.JTextArea;

public interface Player {

	Line getNextMove(Board board, int color, int depth);
	
	// Adds text area for writing heuristics to dialog in step by step mode
	public void setTextArea(JTextArea jta);
	
	// Converts line to string format horizonta/vertical x y
	public static String convertToOutput(Line line) {
		return  ""+ (line.isHorizontal() ? " Horizontal ": " Vertical ")  + line.getX() + " " + line.getY();
	}

}
