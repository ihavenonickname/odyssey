package odyssey.generator.interpreter;

import java.util.Map;

public class Literal<T> implements Expression, ASTNode {
	final T value;

	public Literal(final T value) {
		this.value = value;
	}

	@Override
	public Literal<T> evaluate(final Map<String, Expression> variables) {
		return this;
	}

	public T getValue() {
		return this.value;
	}
}