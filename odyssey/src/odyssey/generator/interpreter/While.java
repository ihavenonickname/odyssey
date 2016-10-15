package odyssey.generator.interpreter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class While implements Statement, ASTNode {
	private final ComparisonOperator condition;
	private final List<Statement> body;

	public While(final ComparisonOperator condition, final Stack<Statement> body) {
		this.condition = condition;
		this.body = body;

		Collections.reverse(this.body);
	}

	@Override
	public void run(final Map<String, Expression> variables) {
		while (true) {
			try {
				if (!(Boolean) this.condition.evaluate(variables).value) {
					break;
				}
			} catch (final Exception e) {
				throw new RuntimeException("Not a boolean expression");
			}

			this.body.forEach(statement -> statement.run(variables));
		}
	}
}