
package compiler;

import compiler.lexer.Lexer;
import compiler.lexer.LexerException;
import compiler.node.*;
import compiler.parser.Parser;
import compiler.parser.ParserException;

import java.io.*;

class Main {

	public static void main(String args[]) {
		String filename = null;
		try {
			filename = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.printf("No argument error: %s\n", e.getMessage());
		}

		Start tree = null;

		PushbackReader reader = null;
		try {
			reader = new PushbackReader(new FileReader(filename), 1024);
		} catch (FileNotFoundException e) {
			System.err.printf("File Not Found error: %s\n", e.getMessage());
			e.printStackTrace();
		}
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
