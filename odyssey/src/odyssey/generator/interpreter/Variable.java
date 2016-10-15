package odyssey.generator.interpreter;

import java.util.Map;

public class Variable implements Expression {
	private final String identifier;

	public Variable(final String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return this.identifier;
	}

	@Override
	public Literal<?> evaluate(final Map<String, Expression> variables) {
		try {
			return variables.get(this.identifier).evaluate(variables);
		} catch (final Exception e) {
			throw new RuntimeException("Variable not declared: " + this.identifier);
		}
	}
}