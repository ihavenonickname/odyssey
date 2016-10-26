package odyssey;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Start {
	public static void main(final String args[]) throws FileNotFoundException, UnsupportedEncodingException {
		if (args.length == 1) {
			Odyssey.interpret(args[0]);

			return;
		}

		if (args.length == 3) {
			switch (args[2]) {
			case "ruby":
				Odyssey.compile(args[0], args[1], TargetLanguage.RUBY);
				break;
			default:
				System.out.println("Target language not supported");
			}

			return;
		}

		Odyssey.compile("C:/Users/GBSM/Documents/Java/lexico/font.txt", "C:/Users/GBSM/Documents/Java/lexico/font.rb", TargetLanguage.RUBY);
		Odyssey.compile("C:/Users/GBSM/Documents/Java/lexico/font.txt", "C:/Users/GBSM/Documents/Java/lexico/font.py", TargetLanguage.PYTHON);
		System.out.println("ok");
	}
}