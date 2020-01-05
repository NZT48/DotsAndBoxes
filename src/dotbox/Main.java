package dotbox;

import javax.swing.JFrame;

public class Main {
	
    private static JFrame frame;
    private static int m;
	private static int n;
	private Player redPlayer, bluePlayer;

	private Player getSolver(int level) {
        if(level == 1) return new EasyComputer();
        //else if(level == 2) return new GreedySolver();
        //else if(level == 3) return new MinimaxSolver();
        //else if(level == 4) return new AlphaBetaSolver();
        //else if(level == 5) return new MCSolver();
        else return null;
    }
	
    @SuppressWarnings("static-access")
	public Main() {
    	frame = new JFrame("Game Dots and Boxes");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.m = 4;
    	this.n = 4;
    	redPlayer = null;
    	bluePlayer = getSolver(1);
    	new Game(frame, m, n, redPlayer, bluePlayer);
    }

	public static void main(String[] args) {
		new Main();
		//new StartPage();
	}

}
