package odyssey.generator.python;

public class ParenthesedExpression implements ASTNode {
	private final ASTNode expression;

	public ParenthesedExpression(final ASTNode expression) {
		this.expression = expression;
	}

	@Override
	public void setIndentation(final int level, final String block) {

	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("(");
		sb.append(this.expression);
		sb.append(")");

		return sb.toString();
	}
}
