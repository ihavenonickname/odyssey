package odyssey.syntactical;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import odyssey.generator.Generator;
import odyssey.lexer.Lexer;
import odyssey.lexer.LexicalException;
import odyssey.lexer.Token;

public class SyntaxAnalyzer {
	private static final Set<Method> statements = new HashSet<Method>();
	private static final Set<Token> comparators = new HashSet<Token>();
	private static final Set<Token> values = new HashSet<Token>();
	private static final Set<Token> types = new HashSet<Token>();

	private final Lexer lexer;
	private final List<Generator> codeGenerators = new ArrayList<Generator>();

	static {
		for (final Token token : Token.values()) {
			if (token.hasAnnotation(Value.class)) {
				values.add(token);
			}

			if (token.hasAnnotation(Comparator.class)) {
				comparators.add(token);
			}

			if (token.hasAnnotation(Type.class)) {
				types.add(token);
			}
		}

		for (final Method method : SyntaxAnalyzer.class.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Statement.class)) {
				statements.add(method);
			}
		}
	}

	public SyntaxAnalyzer(final String filePath) {
		this.lexer = new Lexer(filePath);
	}

	public void attachGenerator(final Generator generator) {
		this.codeGenerators.add(generator);
	}

	public List<Generator> getGenerators() {
		return this.codeGenerators;
	}

	public void analyze() {
		this.lexer.next();

		while (this.lexer.hasNext()) {
			mandatoryStatement();
		}
	}

	private boolean statement() {
		for (final Method method : statements) {
			try {
				method.invoke(this);
				return true;
			} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException | SyntacticalException e) {
				if (e.getCause() instanceof LexicalException) {
					throw (LexicalException) e.getCause();
				}
			}
		}

		return false;
	}

	private void mandatoryStatement() {
		if (!statement()) {
			throwError();
		}
	}

	@Statement
	private void while_() {
		consumeMandatory(Token.WHILE_KEYWORD);

		expressionLevel1();

		consumeMandatory(Token.DO_KEYWORD);

		int counter = 0;

		while (statement()) {
			counter++;
		}

		consumeMandatory(Token.END_KEYWORD);

		final int bodyCounter = counter;

		this.codeGenerators.forEach(g -> g.accumulateWhile(bodyCounter));
	}

	@Statement
	private void if_() {
		consumeMandatory(Token.IF_KEYWORD);

		expressionLevel1();

		consumeMandatory(Token.DO_KEYWORD);

		int counter = 0;

		while (statement()) {
			counter++;
		}

		final int ifBodyCounter = counter;

		counter = 0;

		if (consumeOptional(Token.ELSE_KEYWORD)) {
			while (statement()) {
				counter++;
			}
		}

		final int elseBodyCounter = counter;

		consumeMandatory(Token.END_KEYWORD);

		this.codeGenerators.forEach(g -> g.accumulateIf(ifBodyCounter, elseBodyCounter));
	}

	@Statement
	private void output() {
		consumeMandatory(Token.OUTPUT_KEYWORD);

		expressionLevel1();

		this.codeGenerators.forEach(g -> g.accumulateOutput());
	}

	@Statement
	private void attribution() {
		consumeMandatory(Token.LET_KEYWORD);

		final String id = this.lexer.currentLexeme();

		consumeMandatory(Token.IDENTIFIER);

		this.codeGenerators.forEach(g -> g.accumulateValue(Token.IDENTIFIER, id));

		consumeMandatory(Token.BE_KEYWORD);

		expressionLevel1();

		this.codeGenerators.forEach(g -> g.accumulateAssignment());
	}

	@Statement
	private void input() {
		consumeMandatory(Token.INPUT_KEYWORD);

		final String id = this.lexer.currentLexeme();

		consumeMandatory(Token.IDENTIFIER);

		this.codeGenerators.forEach(g -> g.accumulateValue(Token.IDENTIFIER, id));

		consumeMandatory(Token.AS_KEYWORD);

		type();

		this.codeGenerators.forEach(g -> g.accumulateInput());
	}

	private void type() {
		for (final Token token : types) {
			if (consumeOptional(token)) {
				this.codeGenerators.forEach(g -> g.accumulateType(token));

				return;
			}
		}

		throwError();
	}

	private void expressionLevel1() {
		expressionLevel2();

		if (consumeOptional(Token.OR_KEYWORD)) {
			expressionLevel1();

			this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(Token.OR_KEYWORD));
		}
	}

	private void expressionLevel2() {
		expressionLevel3();

		if (consumeOptional(Token.AND_KEYWORD)) {
			expressionLevel2();

			this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(Token.AND_KEYWORD));
		}
	}

	private void expressionLevel3() {
		expressionLevel4();

		for (final Token token : comparators) {
			if (consumeOptional(token)) {
				expressionLevel4();

				this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(token));

				return;
			}
		}
	}

	private void expressionLevel4() {
		expressionLevel5();

		if (consumeOptional(Token.PLUS_KEYWORD)) {
			expressionLevel4();

			this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(Token.PLUS_KEYWORD));
		}

		if (consumeOptional(Token.MINUS_KEYWORD)) {
			expressionLevel4();

			this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(Token.MINUS_KEYWORD));
		}
	}

	private void expressionLevel5() {
		expressionLevel6();

		if (consumeOptional(Token.TIMES_KEYWORD)) {
			expressionLevel5();

			this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(Token.TIMES_KEYWORD));
		}

		if (consumeOptional(Token.DIVIDED_KEYWORD)) {
			consumeMandatory(Token.BY_KEYWORD);

			expressionLevel5();

			this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(Token.DIVIDED_KEYWORD));
		}

		if (consumeOptional(Token.MODULUS_KEYWORD)) {
			expressionLevel5();

			this.codeGenerators.forEach(g -> g.accumulateBinaryOperation(Token.MODULUS_KEYWORD));
		}
	}

	private void expressionLevel6() {
		if (consumeOptional(Token.OPEN_PARENTHESIS)) {
			expressionLevel1();

			consumeMandatory(Token.CLOSE_PARENTHESIS);

			this.codeGenerators.forEach(g -> g.accumulateExpressionParenthesis());

			return;
		}

		for (final Token token : values) {
			final String lexeme = this.lexer.currentLexeme();

			if (consumeOptional(token)) {
				this.codeGenerators.forEach(g -> g.accumulateValue(token, lexeme));

				return;
			}
		}

		throwError();
	}

	private boolean consumeOptional(final Token token) {
		if (this.lexer.currentToken() != token) {
			return false;
		}

		this.lexer.next();

		return true;
	}

	private void consumeMandatory(final Token token) {
		if (!consumeOptional(token)) {
			throwError();
		}
	}

	private void throwError() {
		throw new SyntacticalException(this.lexer.currentPosition(), this.lexer.currentLexeme());
	}
}
