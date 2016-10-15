package odyssey.generator;

import odyssey.lexer.Token;

public interface Generator {

	public void accumulateWhile(final int bodyCounter);

	public void accumulateIf(final int ifBodyCounter, final int elseBodyCounter);

	public void accumulateOutput();

	public void accumulateInput();

	public void accumulateAssignment();

	public void accumulateBinaryOperation(final Token token);

	public void accumulateExpressionParenthesis();

	public void accumulateValue(final Token token, final String lexeme);

	public void accumulateType(final Token token);

	public StringBuilder getCode();

	public String getDefaultExtension();

}
