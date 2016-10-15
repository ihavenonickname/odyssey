package odyssey.lexer;

import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import odyssey.syntactical.Comparator;
import odyssey.syntactical.Type;
import odyssey.syntactical.Value;

public enum Token {
	LET_KEYWORD("let\\b"),

	BE_KEYWORD("be\\b"),

	WHILE_KEYWORD("while\\b"),

	IF_KEYWORD("if\\b"),

	DO_KEYWORD("do\\b"),

	END_KEYWORD("end\\b"),

	ELSE_KEYWORD("else\\b"),

	AND_KEYWORD("and\\b"),

	OR_KEYWORD("or\\b"),

	INPUT_KEYWORD("input\\b"),

	OUTPUT_KEYWORD("output\\b"),

	AS_KEYWORD("as\\b"),

	// ------------------

	COMMA(","),

	OPEN_PARENTHESIS("\\("),

	CLOSE_PARENTHESIS("\\)"),

	// ------------------

	@Comparator
	DIFFERENT("isnt\\b"),

	@Comparator
	EQUAL("is\\b"),

	@Comparator
	GREATER_EQUAL(">="),

	@Comparator
	LESSER_EQUAL("<="),

	@Comparator
	GREATER(">"),

	@Comparator
	LESSER("<"),

	// ------------------

	ADDITION("\\+"),

	SUBTRACTION("-"),

	MULTIPLICATION("\\*"),

	DIVISION("/"),

	MODULUS("%"),

	// ------------------

	@Type
	TYPE_NUMBER("number\\b"),

	@Type
	TYPE_BOOLEAN("boolean\\b"),

	@Type
	TYPE_TEXT("text\\b"),

	// ------------------

	@Value
	TEXT("\"([^\"]+)?\""),

	@Value
	NUMBER("\\d+(\\.\\d+)?"),

	@Value
	BOOLEAN("((true)|(false))\\b"),

	@Value
	IDENTIFIER("[a-zA-Z](\\w+)?");

	private final Pattern pattern;
	public static final int NOT_FOUND = -1;

	Token(final String regex) {
		this.pattern = Pattern.compile("^" + regex);
	}

	int endOfMatch(final StringBuilder sb) {
		final Matcher m = this.pattern.matcher(sb);

		if (m.find()) {
			return m.end();
		}

		return NOT_FOUND;
	}

	public boolean hasAnnotation(final Class<? extends Annotation> clazz) {
		try {
			return getClass().getField(name()).isAnnotationPresent(clazz);
		} catch (NoSuchFieldException | SecurityException e) {
			return false;
		}
	}
}