package odyssey.lexer;

@SuppressWarnings("serial")
public class LexicalException extends RuntimeException {
	public LexicalException(final int position, final char character) {
		super(String.format("Lexical error at position %d: '%c' not recognized", position, character));
	}
}
