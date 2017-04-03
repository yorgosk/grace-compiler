
package compiler;

import compiler.lexer.Lexer;
import compiler.lexer.LexerException;
import compiler.node.*;
import compiler.parser.Parser;
import compiler.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.InputStreamReader;

class Main {

	public static void main(String args[]) {
		Start tree = null;

		PushbackReader reader = new PushbackReader(new InputStreamReader(System.in), 1024);
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
		}

		System.exit(0);
	}

}
