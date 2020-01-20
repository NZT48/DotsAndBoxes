package etf.dotsandboxes.tn160392d;

import javax.swing.JTextArea;

public interface Player {

	Line getNextMove(Board board, int color, int depth);
	
	public void setTextArea(JTextArea jta);
	
	public static String convertToOutput(Line line) {
		return  " "+ (line.isHorizontal() ? " Horizontal ": " Vertical ")  + line.getX() + " " + line.getY();
	}
}
