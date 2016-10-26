package odyssey;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import odyssey.generator.Generator;
import odyssey.generator.interpreter.Interpreter;
import odyssey.generator.python.PythonGenerator;
import odyssey.generator.ruby.RubyGenerator;
import odyssey.syntactical.SyntaxAnalyzer;

public class Odyssey {

	private static Map<TargetLanguage, Generator> generators = new HashMap<TargetLanguage, Generator>();

	static {
		generators.put(TargetLanguage.RUBY, new RubyGenerator());
		generators.put(TargetLanguage.PYTHON, new PythonGenerator());
	}

	private Odyssey() {

	}

	public static void compile(final String sourcePath, final String targetPath, final TargetLanguage language) throws FileNotFoundException, UnsupportedEncodingException {
		final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(sourcePath);

		syntaxAnalyzer.attachGenerator(generators.get(language));

		syntaxAnalyzer.analyze();

		try (PrintWriter pw = new PrintWriter(targetPath, "UTF-8")) {
			pw.print(generators.get(language).getCode());
		}
	}

	public static void interpret(final String sourcePath) {
		final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(sourcePath);

		final Interpreter interpreter = new Interpreter();

		syntaxAnalyzer.attachGenerator(interpreter);

		syntaxAnalyzer.analyze();

		interpreter.run();
	}
}
