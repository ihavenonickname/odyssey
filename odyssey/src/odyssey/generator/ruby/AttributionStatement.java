package odyssey.generator.ruby;

public class AttributionStatement implements ASTNode {
	private final ASTNode identifier;
	private final ASTNode expression;
	private String indentationBlock;
	private int indentationLevel;

	public AttributionStatement(final ASTNode identifier, final ASTNode expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	@Override
	public void setIndentation(final int level, final String block) {
		this.indentationBlock = block;
		this.indentationLevel = level;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < this.indentationLevel; i++) {
			sb.append(this.indentationBlock);
		}

		sb.append(this.identifier);
		sb.append(" = ");
		sb.append(this.expression);

		return sb.toString();
	}
}
