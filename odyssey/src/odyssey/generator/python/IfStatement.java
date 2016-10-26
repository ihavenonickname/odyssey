package odyssey.generator.python;

import java.util.Stack;

public class IfStatement implements ASTNode {
	private final ASTNode condition;
	private final Stack<ASTNode> ifBranch;
	private final Stack<ASTNode> elseBranch;
	private String indentationBlock;
	private int indentationLevel;

	public IfStatement(final ASTNode condition, final Stack<ASTNode> ifBranch, final Stack<ASTNode> elseBranch) {
		this.condition = condition;
		this.ifBranch = ifBranch;
		this.elseBranch = elseBranch;
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
		sb.append("if ");
		sb.append(this.condition);
		sb.append(":");
		sb.append(ASTNode.NEW_LINE);

		if (!this.ifBranch.isEmpty()) {
			while (!this.ifBranch.isEmpty()) {
				final ASTNode statement = this.ifBranch.pop();
				statement.setIndentation(this.indentationLevel + 1, this.indentationBlock);
				sb.append(statement);
			}
		} else {
			sb.append(indentation);
			sb.append(this.indentationBlock);
			sb.append("pass");
			sb.append(ASTNode.NEW_LINE);
		}

		if (!this.elseBranch.isEmpty()) {
			sb.append(indentation);
			sb.append("else:");
			sb.append(ASTNode.NEW_LINE);

			while (!this.elseBranch.isEmpty()) {
				final ASTNode statement = this.elseBranch.pop();
				statement.setIndentation(this.indentationLevel + 1, this.indentationBlock);
				sb.append(statement);
			}
		}

		return sb.toString();
	}
}
