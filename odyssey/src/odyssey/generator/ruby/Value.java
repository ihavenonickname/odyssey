package odyssey.generator.ruby;

public class Value implements ASTNode {
	private final String name;

	public Value(final String name) {
		this.name = name;
	}

	@Override
	public void setIndentation(final int level, final String block) {

	}

	@Override
	public String toString() {
		return this.name;
	}
}
