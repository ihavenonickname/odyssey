package odyssey.generator.interpreter;

import java.util.Map;

public interface Statement {
	public void run(Map<String, Expression> variables);
}