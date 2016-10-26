package odyssey.generator.interpreter;

import java.util.Map;

public class ArithmecticOperator implements Expression {
	private final Expression leftExpression;
	private final Expression rightExpression;
	private final ArithmecticOperation operation;

	public ArithmecticOperator(final Expression leftExpression, final Expression rightExpression, final ArithmecticOperation operation) {
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
		this.operation = operation;
	}

	@Override
	public Literal<Double> evaluate(final Map<String, Expression> variables) {
		final Double left = (Double) this.leftExpression.evaluate(variables).getValue();
		final Double right = (Double) this.rightExpression.evaluate(variables).getValue();

		return new Literal<Double>(this.operation.calc(left, right));
	}
}