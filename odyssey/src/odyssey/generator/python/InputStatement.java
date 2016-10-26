package odyssey.generator.python;

import odyssey.lexer.Token;

public class InputStatement implements ASTNode {
	private final Token type;
	private final ASTNode identifier;
	private String indentationBlock;
	private int indentationLevel;

	public InputStatement(final ASTNode identifier, final Token type) {
		this.type = type;
		this.identifier = identifier;
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

		if (this.type == Token.TYPE_NUMBER) {
			sb.append("float(input())");
		} else if (this.type == Token.TYPE_BOOLEAN) {
			sb.append("input().lower() == 'true'");
		} else if (this.type == Token.TYPE_TEXT) {
			sb.append("input()");
		}

		sb.append(ASTNode.NEW_LINE);

		return sb.toString();
	}
}
