package dotbox;

public class Line {
	
	private int x, y;
	private boolean horizontal;
	
	public Line() {
		x = y = -1;
		horizontal = false;
	}
	
	
	public Line(int x, int y, boolean horizontal) {
		super();
		this.x = x;
		this.y = y;
		this.horizontal = horizontal;
	}

	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public boolean isHorizontal() {
		return horizontal;
	}

	

}
