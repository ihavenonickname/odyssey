package odyssey.generator.python;

public class BinaryOperation implements ASTNode {
	private final ASTNode left;
	private final ASTNode right;
	private final String op;

	public BinaryOperation(final ASTNode left, final String op, final ASTNode right) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	@Override
	public void setIndentation(final int level, final String block) {

	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("(");
		sb.append(this.left);
		sb.append(this.op);
		sb.append(this.right);
		sb.append(")");

		return sb.toString();
	}

}
