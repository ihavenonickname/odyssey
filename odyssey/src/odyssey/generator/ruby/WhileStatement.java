package odyssey.generator.ruby;

import java.util.Stack;

public class WhileStatement implements ASTNode {
	private final ASTNode condition;
	private final Stack<ASTNode> body;
	private String indentationBlock;
	private int indentationLevel;

	public WhileStatement(final ASTNode condition, final Stack<ASTNode> body) {
		this.condition = condition;
		this.body = body;
	}

	@Override
	public void setIndentation(final int level, final String block) {
		this.indentationBlock = block;
		this.indentationLevel = level;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final StringBuilder indentation = new StringBuilder();

		for (int i = 0; i < this.indentationLevel; i++) {
			indentation.append(this.indentationBlock);
		}

		sb.append(indentation);
		sb.append("while ");
		sb.append(this.condition);
		sb.append(" do");
		sb.append(System.lineSeparator());

		while (!this.body.isEmpty()) {
			final ASTNode statement = this.body.pop();
			statement.setIndentation(this.indentationLevel + 1, this.indentationBlock);
			sb.append(statement);
			sb.append(System.lineSeparator());
		}

		sb.append(indentation);
		sb.append("end");

		return sb.toString();
	}
}
