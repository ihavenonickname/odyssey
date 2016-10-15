package odyssey.generator.interpreter;

import java.util.Map;

public class Assignment implements Statement, ASTNode {
	private final String identifier;
	private final Expression expression;

	public Assignment(final String identifier, final Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	@Override
	public void run(final Map<String, Expression> variables) {
		variables.put(this.identifier, this.expression.evaluate(variables));
	}
}