package odyssey.generator.ruby;

import odyssey.lexer.Token;

public class TypeValue implements ASTNode {
	private final Token token;

	public TypeValue(final Token token) {
		this.token = token;
	}

	@Override
	public void setIndentation(final int level, final String block) {

	}

	public Token getToken() {
		return this.token;
	}
}
