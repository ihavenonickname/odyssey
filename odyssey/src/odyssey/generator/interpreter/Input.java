package odyssey.generator.interpreter;

import java.util.Map;

public class Input<T> implements Statement, ASTNode {
	private final String identifier;
	private final Converter<T> converter;

	public Input(final String identifier, final Converter<T> converter) {
		this.identifier = identifier;
		this.converter = converter;
	}

	@Override
	public void run(final Map<String, Expression> variables) {
		variables.put(this.identifier, new Literal<T>(getInput()));
	}

	private T getInput() {
		while (true) {
			try {
				return this.converter.convert(Interpreter.in.nextLine());
			} catch (final Exception e) {

			}
		}
	}
}