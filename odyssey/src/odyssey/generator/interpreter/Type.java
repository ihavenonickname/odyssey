package odyssey.generator.interpreter;

import odyssey.lexer.Token;

public class Type implements ASTNode {
	private final Token token;

	public Type(final Token token) {
		this.token = token;
	}

	public Token getToken() {
		return this.token;
	}
}
