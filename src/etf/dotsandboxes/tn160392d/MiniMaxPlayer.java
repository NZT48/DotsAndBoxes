package etf.dotsandboxes.tn160392d;

import javax.swing.JTextArea;

public abstract class MiniMaxPlayer implements Player{
	
	final static int MIN = -1000000000, MAX = 1000000000;

    protected int referenceColor;

    protected JTextArea hWindow = null;
    
    public void setTextArea(JTextArea jta) {
    	hWindow = jta;
    }

    // Function that caluculate the heuristic of given board
    protected abstract int heuristic(final Board board, int color);
    
	public abstract Line getNextMove(Board board, int color, int depth);
	
}
