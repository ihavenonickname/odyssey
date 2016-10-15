package odyssey.generator.interpreter;

import java.util.Map;

public interface Expression extends ASTNode {
	public Literal<?> evaluate(Map<String, Expression> variables);
}