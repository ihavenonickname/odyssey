package odyssey.lexer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Lexer {
	private static Set<Character> blankChars = new HashSet<Character>();

	private final StringBuilder input = new StringBuilder();
	private Token token;
	private String lexeme;
	private boolean exhausted = false;
	private final int originalSize;

	static {
		blankChars.add('\r');
		blankChars.add('\n');
		blankChars.add((char) 8);
		blankChars.add((char) 9);
		blankChars.add((char) 11);
		blankChars.add((char) 12);
		blankChars.add((char) 32);
	}

	public Lexer(final String filePath) {
		try {
			final byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			this.input.append(new String(encoded, Charset.defaultCharset()));
			this.originalSize = this.input.length();
		} catch (final IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void next() {
		if (this.exhausted) {
			return;
		}

		if (this.input.length() == 0) {
			this.exhausted = true;
			return;
		}

		ignoreWhiteSpaces();

		if (findNextToken()) {
			if (this.input.length() == 0) {
				this.exhausted = true;
			}

			return;
		}

		this.exhausted = true;

		if (this.input.length() > 0) {
			throw new LexicalException(currentPosition(), this.input.charAt(0));
		}
	}

	private void ignoreWhiteSpaces() {
		int charsToDelete = 0;

		while (charsToDelete < this.input.length() && blankChars.contains(this.input.charAt(charsToDelete))) {
			charsToDelete++;
		}

		if (charsToDelete > 0) {
			this.input.delete(0, charsToDelete);
		}
	}

	private boolean findNextToken() {
		for (final Token t : Token.values()) {
			final int end = t.endOfMatch(this.input);

			if (end != Token.NOT_FOUND) {
				this.token = t;
				this.lexeme = this.input.substring(0, end);
				this.input.delete(0, end);
				return true;
			}
		}

		return false;
	}

	public Token currentToken() {
		return this.token;
	}

	public String currentLexeme() {
		return this.lexeme;
	}

	public boolean hasNext() {
		return !this.exhausted;
	}

	public int currentPosition() {
		return this.originalSize - this.input.length();
	}

	public void forEach(final LexicalConsumer consumer) {
		for (next(); hasNext(); next()) {
			consumer.consume(currentToken(), currentLexeme());
		}
	}
}