package odyssey.lexer;

public interface LexicalConsumer {
	public void consume(Token token, String lexeme);
}
