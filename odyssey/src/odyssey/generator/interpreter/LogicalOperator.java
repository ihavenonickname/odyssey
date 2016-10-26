package odyssey.generator.interpreter;

import java.util.Map;

public class LogicalOperator implements Expression {
	private final Expression leftExpression;
	private final Expression rightExpression;
	private final LogicalOperation operation;

	public LogicalOperator(final Expression leftExpression, final Expression rightExpression, final LogicalOperation operation) {
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
		this.operation = operation;
	}

	@Override
	public Literal<?> evaluate(final Map<String, Expression> variables) {
		final Boolean left = (Boolean) this.leftExpression.evaluate(variables).getValue();
		final Boolean right = (Boolean) this.rightExpression.evaluate(variables).getValue();

		return new Literal<Boolean>(this.operation.calc(left, right));
	}

}
