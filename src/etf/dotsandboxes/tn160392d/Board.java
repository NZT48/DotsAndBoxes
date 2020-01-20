package etf.dotsandboxes.tn160392d;

import java.awt.Point;
import java.util.ArrayList;


public class Board {
	
    public final static int RED = 0;
    public final static int BLUE = 1;
    public final static int BLACK = 2;
    public final static int BLANK = 3;
	
	private int[][] boxes;
	private int[][] hLines;
	private int[][] vLines;
	
	private int n, m, redScore, blueScore;

	public Board(int m, int n) {
		super();
		this.m = m;
		this.n = n;
		boxes = new int[m-1][n-1];
		hLines = new int[m][n-1];
		vLines = new int[m-1][n];
		redScore = blueScore = 0;
		fill(hLines, BLANK);
		fill(vLines, BLANK);
		fill(boxes, BLANK);
	}
	
    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getWinner() {
		if(redScore > blueScore) return RED;
		else if(redScore < blueScore) return BLUE;
		else return BLANK;
	}

    public ArrayList<Line> getAvailableMoves() {
        ArrayList<Line> ret = new ArrayList<Line>();
        for(int i = 0; i < m;i++)
            for(int j = 0; j < (n-1); j++)
                if(hLines[i][j] == BLANK)
                    ret.add(new Line(i,j,true));
        for(int i = 0; i < (m-1); i++)
            for(int j = 0; j < n; j++)
                if(vLines[i][j] == BLANK)
                    ret.add(new Line(i,j,false));
        return ret;
    }

    public Board getNewBoard(Line line, int color) {
        Board ret = clone();
        if(line.isHorizontal())
            ret.setHLine(line.getX(), line.getY(), color);
        else
            ret.setVLine(line.getX(), line.getY(), color);
        return ret;
    }
    
    // Get number of lines of box at given location
    private int getLinesCount(int i, int j) {
        int count = 0;
        if(hLines[i][j] == BLACK) count++;
        if(hLines[i+1][j] == BLACK) count++;
        if(vLines[i][j] == BLACK) count++;
        if(vLines[i][j+1] == BLACK) count++;
        return count;
    }
    
    // Get number of boxes with nSides number of lines
	public int getBoxCount(int nSides) {
        int count = 0;
        for(int i = 0; i < (m-1); i++)
            for(int j = 0; j < (n-1); j++) {
                if(getLinesCount(i, j) == nSides)
                    count++;
            }
        return count;
    }
   	
	public ArrayList<Point> setHLine(int x, int y, int turn) {
		hLines[x][y] = BLACK;
		ArrayList<Point> ret = new ArrayList<Point>();
		// Check if the square below is closed
		if(x < (m-1) && vLines[x][y] == BLACK && vLines[x][y+1] == BLACK && hLines[x+1][y] == BLACK) {
			boxes[x][y] = turn;
			ret.add(new Point(x,y));
			if(turn == RED) redScore++;
			else blueScore++;
		}
		// Check if the upper square is closed
		if(x >0 && vLines[x-1][y+1] == BLACK && vLines[x-1][y] == BLACK && hLines[x-1][y] == BLACK) {
			boxes[x-1][y] = turn;
			ret.add(new Point(x-1, y));
			if(turn == RED) redScore++;
			else blueScore++;
		}
		
		return ret;
	}

	public ArrayList<Point> setVLine(int x, int y, int turn) {
		vLines[x][y] = BLACK;
		ArrayList<Point> ret = new ArrayList<Point>();
		// Check if the right square is closed
		if(y < (n-1) && hLines[x][y] == BLACK && hLines[x+1][y] == BLACK && vLines[x][y+1] == BLACK) {
			boxes[x][y] = turn;
			ret.add(new Point(x,y));
			if(turn == RED) redScore++;
			else blueScore++;
		}
		// Check if the left square is closed
		if(y>0 && hLines[x][y-1] == BLACK && hLines[x+1][y-1] == BLACK && vLines[x][y-1] == BLACK) {
			boxes[x][y-1] = turn;
			ret.add(new Point(x, y-1));
			if(turn == RED) redScore++;
			else blueScore++;
		}
		
		return ret;
	}
	
	// Fill all elements of array with given value
	private void fill(int[][] array, int val) {
        for(int i = 0; i < array.length; i++)
            for(int j = 0; j < array[i].length; j++)
                array[i][j]=val;
    }

	public boolean isComplete() {
		if(redScore + blueScore == (m-1)*(n-1)) {
			return true;
		} else return false;
	}
   
	// Used for get next board
    public Board clone() {
        Board cloned = new Board(m,n);

        for(int i = 0; i < (m); i++)
            for(int j = 0; j < (n-1); j++)
                cloned.hLines[i][j] = hLines[i][j];

        for(int i = 0; i < (m-1); i++)
            for(int j = 0; j < (n); j++)
                cloned.vLines[i][j] = vLines[i][j];

        for(int i = 0; i < (m-1); i++)
            for(int j = 0; j < (n-1); j++)
                cloned.boxes[i][j] = boxes[i][j];

        cloned.redScore = redScore;
        cloned.blueScore = blueScore;

        return cloned;
    }
    
}
