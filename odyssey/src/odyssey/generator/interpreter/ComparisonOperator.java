package odyssey.generator.interpreter;

import java.util.Map;

public class ComparisonOperator implements Expression {
	private final Expression leftExpression;
	private final Expression rightExpression;
	private final ComparisonOperation operation;

	public ComparisonOperator(final Expression leftExpression, final Expression rightExpression, final ComparisonOperation operation) {
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
		this.operation = operation;
	}

	@Override
	public Literal<Boolean> evaluate(final Map<String, Expression> variables) {
		final Double left = (Double) this.leftExpression.evaluate(variables).getValue();
		final Double right = (Double) this.rightExpression.evaluate(variables).getValue();

		return new Literal<Boolean>(this.operation.calc(left, right));
	}
}