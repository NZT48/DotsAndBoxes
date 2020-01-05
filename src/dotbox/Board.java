package dotbox;

import java.awt.Point;
import java.util.ArrayList;


public class Board {
	
	// Pretvoriti ovo u Enum
    public final static int RED = 0;
    public final static int BLUE = 1;
    public final static int BLACK = 2;
    public final static int BLANK = 3;
	
	private int[][] boxes;
	private int[][] hLines;
	private int[][] vLines;
	
	private int n, m, scoreP1, scoreP2;

	public Board(int m, int n) {
		super();
		this.m = m;
		this.n = n;
		boxes = new int[m-1][n-1];
		hLines = new int[m][n-1];
		vLines = new int[m-1][n];
		scoreP1 = scoreP2 = 0;
		fill(hLines, BLANK);
		fill(vLines, BLANK);
		fill(boxes, BLANK);
		
	}
	
	private void fill(int[][] array, int val) {
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=val;
    }

	public boolean isComplete() {
		if(scoreP1 + scoreP2 == (m-1)*(n-1)) {
			return true;
		} else return false;
	}

	public int getWinner() {
		if(scoreP1 > scoreP2) return RED;
		else if(scoreP1 < scoreP2) return BLUE;
		else return BLANK;
	}

	public ArrayList<Point> setHLine(int x, int y, int turn) {
		// Postavljamo boju na novoj liniji
		hLines[x][y] = BLACK;
		//Niz tacaka pravimo
		ArrayList<Point> ret = new ArrayList<Point>();
		//proveri donji kvadrat da li je zatvoren
		if(x < (m-1) && vLines[x][y]== BLACK && vLines[x][y+1] == BLACK && hLines[x+1][y] == BLACK) {
			boxes[x][y] = turn;
			ret.add(new Point(x,y));
			if(turn == RED) scoreP1++;
			else scoreP2++;
		}
		//proveri gornji kvadrat da li je zatvoren
		if(x >0 && vLines[x-1][y+1] == BLACK && vLines[x-1][y] == BLACK && hLines[x-1][y] == BLACK) {
			boxes[x-1][y] = turn;
			ret.add(new Point(x-1, y));
			if(turn == RED) scoreP1++;
			else scoreP2++;
		}
		
		return ret;
	}

	public ArrayList<Point> setVLine(int x, int y, int turn) {
		// Postavljamo boju na novoj liniji
		vLines[x][y] = BLACK;
		//Niz tacaka pravimo
		ArrayList<Point> ret = new ArrayList<Point>();
		// proveri desni kvadrat da li je zatvoren
		if(y < (n-1) && hLines[x][y]== BLACK && hLines[x+1][y] == BLACK && vLines[x][y+1] == BLACK) {
			boxes[x][y] = turn;
			ret.add(new Point(x,y));
			if(turn == RED) scoreP1++;
			else scoreP2++;
		}
		// proveri levi kvadrat da li je zatvoren
		if(y>0 && hLines[x][y-1] == BLACK && hLines[x+1][y-1] == BLACK && vLines[x][y-1] == BLACK) {
			boxes[x][y-1] = turn;
			ret.add(new Point(x, y-1));
			if(turn == RED) scoreP1++;
			else scoreP2++;
		}
		
		return ret;
	}
	
    public int getRedScore() {
        return scoreP1;
    }

    public int getBlueScore() {
        return scoreP2;
    }
	
    
    
    // Proveriti granice ovde
    public ArrayList<Line> getAvailableMoves() {
        ArrayList<Line> ret = new ArrayList<Line>();
        for(int i=0; i<m;i++)
            for(int j=0; j<(n-1); j++)
                if(hLines[i][j] == BLANK)
                    ret.add(new Line(i,j,true));
        for(int i=0; i<(m-1); i++)
            for(int j=0; j<n; j++)
                if(vLines[i][j] == BLANK)
                    ret.add(new Line(i,j,false));
        return ret;
    }

    public Board clone() {
        Board cloned = new Board(m,n);

        for(int i=0; i<(m); i++)
            for(int j=0; j<(n-1); j++)
                cloned.hLines[i][j] = hLines[i][j];

        for(int i=0; i<(m-1); i++)
            for(int j=0; j<(n); j++)
                cloned.vLines[i][j] = vLines[i][j];

        for(int i=0; i<(m-1); i++)
            for(int j=0; j<(n-1); j++)
                cloned.boxes[i][j] = boxes[i][j];

        cloned.scoreP1 = scoreP1;
        cloned.scoreP2 = scoreP2;

        return cloned;
    }
    
    //Izmeniti
    public Board getNewBoard(Line edge, int color) {
        Board ret = clone();
        if(edge.isHorizontal())
            ret.setHLine(edge.getX(), edge.getY(), color);
        else
            ret.setVLine(edge.getX(), edge.getY(), color);
        return ret;
    }
    
    public int getScore(int color) {
        if(color == RED) return scoreP2;
        else return scoreP2;
    }
    
    public static int changeColor(int color) {
        if(color == RED)
            return BLUE;
        else
            return RED;
    }
    
    private int getLinesCount(int i, int j) {
        int count = 0;
        if(hLines[i][j] == BLACK) count++;
        if(hLines[i+1][j] == BLACK) count++;
        if(vLines[i][j] == BLACK) count++;
        if(vLines[i][j+1] == BLACK) count++;
        return count;
    }
    
    public int getBoxCount(int nSides) {
        int count = 0;
        for(int i=0; i<(m-1); i++)
            for(int j=0; j<(n-1); j++) {
                if(getLinesCount(i, j) == nSides)
                    count++;
            }
        return count;
    }
}
