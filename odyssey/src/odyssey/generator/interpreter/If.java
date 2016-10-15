package odyssey.generator.interpreter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class If implements Statement, ASTNode {
	private final ComparisonOperator condition;
	private final List<Statement> ifBranch;
	private final List<Statement> elseBranch;

	public If(final ComparisonOperator condition, final List<Statement> ifBranch, final List<Statement> elseBranch) {
		this.condition = condition;
		this.ifBranch = ifBranch;
		this.elseBranch = elseBranch;

		Collections.reverse(ifBranch);
		Collections.reverse(elseBranch);
	}

	@Override
	public void run(final Map<String, Expression> variables) {
		Boolean resultCondition;

		try {
			resultCondition = (Boolean) this.condition.evaluate(variables).value;
		} catch (final Exception e) {
			throw new RuntimeException("Not a boolean expression");
		}

		(resultCondition ? this.ifBranch : this.elseBranch).forEach(statement -> statement.run(variables));
	}
}