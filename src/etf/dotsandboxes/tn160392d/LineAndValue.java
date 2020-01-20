package etf.dotsandboxes.tn160392d;

public class LineAndValue {
	@SuppressWarnings("unused")
	private Line line;
	private int Value;

	LineAndValue(Line line, int w) {
		this.line = line;
		Value = w;
	}

	public int getValue() {
		return Value;
	}
}
