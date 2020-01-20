package etf.dotsandboxes.tn160392d;

public class LineAndValue {
	
	@SuppressWarnings("unused")
	private Line line;
	private int value;

	LineAndValue(Line line, int value) {
		this.line = line;
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
