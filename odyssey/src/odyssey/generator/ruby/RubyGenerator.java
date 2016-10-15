package odyssey.generator.ruby;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import odyssey.generator.Generator;
import odyssey.lexer.Token;

public class RubyGenerator implements Generator {
	private static Map<Token, String> binaryOperators = new HashMap<Token, String>();

	private final Stack<ASTNode> semanticStack = new Stack<ASTNode>();
	private final String identationBlock;
	private final String newLine = System.lineSeparator();

	static {
		binaryOperators.put(Token.EQUAL, " == ");
		binaryOperators.put(Token.DIFFERENT, " != ");
		binaryOperators.put(Token.GREATER, " > ");
		binaryOperators.put(Token.GREATER_EQUAL, " >= ");
		binaryOperators.put(Token.LESSER, " < ");
		binaryOperators.put(Token.LESSER_EQUAL, " <= ");
		binaryOperators.put(Token.ADDITION, " + ");
		binaryOperators.put(Token.SUBTRACTION, " - ");
		binaryOperators.put(Token.MULTIPLICATION, " * ");
		binaryOperators.put(Token.DIVISION, " / ");
		binaryOperators.put(Token.MODULUS, " % ");
	}

	public RubyGenerator() {
		this("  ");
	}

	public RubyGenerator(final String identationBlock) {
		this.identationBlock = identationBlock;
	}

	@Override
	public void accumulateWhile(final int bodyCounter) {
		final Stack<ASTNode> body = new Stack<ASTNode>();

		for (int i = 0; i < bodyCounter; i++) {
			final ASTNode statement = this.semanticStack.pop();
			body.push(statement);
		}

		final ASTNode condition = this.semanticStack.pop();

		this.semanticStack.push(new WhileStatement(condition, body));
	}

	@Override
	public void accumulateIf(final int ifBodyCounter, final int elseBodyCounter) {
		final Stack<ASTNode> ifBranch = new Stack<ASTNode>();
		final Stack<ASTNode> elseBranch = new Stack<ASTNode>();

		for (int i = 0; i < elseBodyCounter; i++) {
			final ASTNode statement = this.semanticStack.pop();
			elseBranch.push(statement);
		}

		for (int i = 0; i < ifBodyCounter; i++) {
			final ASTNode statement = this.semanticStack.pop();
			ifBranch.push(statement);
		}

		final ASTNode condition = this.semanticStack.pop();

		this.semanticStack.push(new IfStatement(condition, ifBranch, elseBranch));
	}

	@Override
	public void accumulateOutput() {
		final ASTNode expression = this.semanticStack.pop();

		this.semanticStack.push(new OutputStatement(expression));
	}

	@Override
	public void accumulateInput() {
		final TypeValue type = (TypeValue) this.semanticStack.pop();
		final ASTNode identifier = this.semanticStack.pop();

		this.semanticStack.push(new InputStatement(identifier, type.getToken()));
	}

	@Override
	public void accumulateAssignment() {
		final ASTNode expression = this.semanticStack.pop();
		final ASTNode identifier = this.semanticStack.pop();

		this.semanticStack.push(new AttributionStatement(identifier, expression));
	}

	@Override
	public void accumulateBinaryOperation(final Token token) {
		final ASTNode right = this.semanticStack.pop();
		final ASTNode left = this.semanticStack.pop();
		final String op = binaryOperators.get(token);

		this.semanticStack.push(new BinaryOperation(left, op, right));
	}

	@Override
	public void accumulateExpressionParenthesis() {
		final ASTNode expression = this.semanticStack.pop();

		this.semanticStack.push(new ParenthesedExpression(expression));
	}

	@Override
	public void accumulateValue(final Token token, final String lexeme) {
		this.semanticStack.push(new Value(lexeme));
	}

	@Override
	public void accumulateType(final Token token) {
		this.semanticStack.push(new TypeValue(token));
	}

	@Override
	public StringBuilder getCode() {
		final StringBuilder sb = new StringBuilder();

		for (final ASTNode node : this.semanticStack) {
			node.setIndentation(0, this.identationBlock);
			sb.append(node).append(this.newLine).append(this.newLine);
		}

		return sb;
	}

	@Override
	public String getDefaultExtension() {
		return "rb";
	}
}
