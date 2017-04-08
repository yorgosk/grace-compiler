
package compiler;

import compiler.lexer.Lexer;
import compiler.lexer.LexerException;
import compiler.node.*;
import compiler.parser.Parser;
import compiler.parser.ParserException;

import java.io.*;

class Main {

	public static void main(String args[]) {
		int argsCount = args.length;
		String filename;
		PushbackReader reader = null;

		if (argsCount == 0) {
			reader = new PushbackReader(new InputStreamReader(System.in), 1024);
		} else if (argsCount == 1) {
			filename = args[0];
			try {
				reader = new PushbackReader(new FileReader(filename), 1024);
			} catch (FileNotFoundException e) {
				System.err.printf("File Not Found error: %s\n", e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.err.printf("Can't compile more than one files at once.\n");
			System.exit(-1);
		}

		Start tree = null;

		try {
			Parser p = new Parser(new Lexer(reader));
			tree = p.parse();
		} catch (LexerException e) {
			System.err.printf("Lexing error: %s\n", e.getMessage());
		} catch (IOException e) {
			System.err.printf("I/O error: %s\n", e.getMessage());
			e.printStackTrace();
		} catch (ParserException e) {
			System.err.printf("Parsing error: %s\n", e.getMessage());
		}

		try {
			System.out.printf("\nPrinting Parsing Tree:\n\n");
			tree.apply(new PTPrintingVisitor());
		} catch (NullPointerException e) {
			System.err.printf("Null pointer Exception: %s\n", e.getMessage());
			e.printStackTrace();
		}

		System.exit(0);
	}

}
