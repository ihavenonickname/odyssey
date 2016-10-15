package odyssey.generator.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import odyssey.generator.Generator;
import odyssey.lexer.Token;

public class Interpreter implements Generator {
	static Scanner in = new Scanner(System.in);

	@SuppressWarnings("rawtypes")
	private static Map<Token, Converter> converters = new HashMap<Token, Converter>();
	private static Map<Token, ArithmecticOperation> arithmeticOperations = new HashMap<Token, ArithmecticOperation>();
	private static Map<Token, ComparisonOperation> comparisonOperations = new HashMap<Token, ComparisonOperation>();

	private final Stack<ASTNode> semancticStack = new Stack<ASTNode>();

	static {
		arithmeticOperations.put(Token.ADDITION, (a, b) -> a + b);
		arithmeticOperations.put(Token.SUBTRACTION, (a, b) -> a - b);
		arithmeticOperations.put(Token.MULTIPLICATION, (a, b) -> a * b);
		arithmeticOperations.put(Token.DIVISION, (a, b) -> a / b);
		arithmeticOperations.put(Token.MODULUS, (a, b) -> a % b);

		comparisonOperations.put(Token.EQUAL, (a, b) -> a.equals(b));
		comparisonOperations.put(Token.DIFFERENT, (a, b) -> !a.equals(b));
		comparisonOperations.put(Token.GREATER, (a, b) -> a > b);
		comparisonOperations.put(Token.GREATER_EQUAL, (a, b) -> a >= b);
		comparisonOperations.put(Token.LESSER, (a, b) -> a < b);
		comparisonOperations.put(Token.LESSER_EQUAL, (a, b) -> a <= b);

		converters.put(Token.TYPE_TEXT, text -> text);
		converters.put(Token.TYPE_NUMBER, text -> Double.parseDouble(text));
		converters.put(Token.TYPE_BOOLEAN, text -> text.toLowerCase().equals("true"));
	}

	@Override
	public void accumulateWhile(final int bodyCounter) {
		final Stack<Statement> body = new Stack<Statement>();

		for (int i = 0; i < bodyCounter; i++) {
			final Statement statement = (Statement) this.semancticStack.pop();
			body.push(statement);
		}

		final ComparisonOperator condition = (ComparisonOperator) this.semancticStack.pop();

		this.semancticStack.push(new While(condition, body));
	}

	@Override
	public void accumulateIf(final int ifBodyCounter, final int elseBodyCounter) {
		final Stack<Statement> elseBody = new Stack<Statement>();
		final Stack<Statement> ifBody = new Stack<Statement>();

		for (int i = 0; i < elseBodyCounter; i++) {
			final Statement statement = (Statement) this.semancticStack.pop();
			elseBody.add(statement);
		}

		for (int i = 0; i < ifBodyCounter; i++) {
			final Statement statement = (Statement) this.semancticStack.pop();
			ifBody.add(statement);
		}

		final ComparisonOperator condition = (ComparisonOperator) this.semancticStack.pop();

		this.semancticStack.push(new If(condition, ifBody, elseBody));
	}

	@Override
	public void accumulateOutput() {
		final Expression expression = (Expression) this.semancticStack.pop();

		this.semancticStack.push(new Output(expression));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void accumulateInput() {
		final Type type = (Type) this.semancticStack.pop();
		final Variable variable = (Variable) this.semancticStack.pop();

		if (!converters.containsKey(type.getToken())) {
			throwError("Not a valid type");
		}

		this.semancticStack.push(new Input(variable.getName(), converters.get(type.getToken())));
	}

	@Override
	public void accumulateAssignment() {
		final Expression expression = (Expression) this.semancticStack.pop();
		final Variable variable = (Variable) this.semancticStack.pop();

		this.semancticStack.push(new Assignment(variable.getName(), expression));
	}

	@Override
	public void accumulateBinaryOperation(final Token token) {
		final Expression right = (Expression) this.semancticStack.pop();
		final Expression left = (Expression) this.semancticStack.pop();

		if (arithmeticOperations.containsKey(token)) {
			final ArithmecticOperation op = arithmeticOperations.get(token);

			this.semancticStack.push(new ArithmecticOperator(left, right, op));
		} else if (comparisonOperations.containsKey(token)) {
			final ComparisonOperation op = comparisonOperations.get(token);

			this.semancticStack.push(new ComparisonOperator(left, right, op));
		} else {
			throwError("Not a valid arithmetic operation");
		}
	}

	@Override
	public void accumulateExpressionParenthesis() {
		// TODO Auto-generated method stub

	}

	@Override
	public void accumulateValue(final Token token, final String lexeme) {
		if (token == Token.BOOLEAN) {
			final Literal<Boolean> literal = new Literal<Boolean>(lexeme.toLowerCase().equals("true"));
			this.semancticStack.push(literal);
		} else if (token == Token.NUMBER) {
			final Literal<Double> literal = new Literal<Double>(Double.parseDouble(lexeme));
			this.semancticStack.push(literal);
		} else if (token == Token.TEXT) {
			final Literal<String> literal = new Literal<String>(lexeme.substring(1, lexeme.length() - 1));
			this.semancticStack.push(literal);
		} else if (token == Token.IDENTIFIER) {
			final Variable variable = new Variable(lexeme);
			this.semancticStack.push(variable);
		} else {
			throwError("Not a valid type");
		}
	}

	@Override
	public void accumulateType(final Token token) {
		this.semancticStack.push(new Type(token));
	}

	@Override
	public StringBuilder getCode() {
		throw new UnsupportedOperationException("Cannot get code from Interpreter");
	}

	@Override
	public String getDefaultExtension() {
		throw new UnsupportedOperationException("Cannot get code from Interpreter");
	}

	public void run() {
		final Map<String, Expression> variables = new HashMap<String, Expression>();

		for (final ASTNode node : this.semancticStack) {
			((Statement) node).run(variables);
		}
	}

	private void throwError(final String message) {
		throw new RuntimeException("Runtime error: " + message);
	}
}
