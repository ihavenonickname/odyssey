package odyssey.generator.python;

public interface ASTNode {
	public static final String NEW_LINE = System.lineSeparator();

	public void setIndentation(int level, String block);
}
