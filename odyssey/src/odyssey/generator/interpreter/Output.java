package odyssey.generator.interpreter;

import java.util.Map;

public class Output implements Statement, ASTNode {
	private final Expression expression;

	public Output(final Expression expression) {
		this.expression = expression;
	}

	@Override
	public void run(final Map<String, Expression> variables) {
		System.out.println(this.expression.evaluate(variables).value);
	}
}