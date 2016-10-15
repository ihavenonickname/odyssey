package odyssey.generator.ruby;

public class OutputStatement implements ASTNode {
	private final ASTNode expression;
	private String indentationBlock;
	private int indentationLevel;

	public OutputStatement(final ASTNode expression) {
		this.expression = expression;
	}

	public void setIndentationBlock(final String indentationBlock) {
		this.indentationBlock = indentationBlock;
	}

	public void setIndentationLevel(final int indentationLevel) {
		this.indentationLevel = indentationLevel;
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

		sb.append("print ");
		sb.append(this.expression);

		return sb.toString();
	}

}
