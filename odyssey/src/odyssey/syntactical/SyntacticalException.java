package odyssey.syntactical;

@SuppressWarnings("serial")
class SyntacticalException extends RuntimeException {
	public SyntacticalException(final int position, final String lexeme) {
		super(String.format("Syntactical error at position %d: '%s' not recognized", position, lexeme));
	}
}
