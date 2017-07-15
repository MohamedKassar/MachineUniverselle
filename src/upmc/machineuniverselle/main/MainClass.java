package upmc.machineuniverselle.main;

import java.io.PrintStream;

import upmc.machineuniverselle.exceptions.InvalidProgramException;
import upmc.machineuniverselle.interpreter.Interpreter;
import upmc.machineuniverselle.lexparse.BinaryFileLexParse;
import upmc.machineuniverselle.lexparse.CharacterFileLexParse;
import upmc.machineuniverselle.lexparse.ILexParse;

public class MainClass {

	private static void runFile(String file) {
		try {
			Interpreter interpreter;
			ILexParse lexParse;
			/*
			 * we can print out put in a file
			 */
			PrintStream output = new PrintStream(System.err);
			
			/*
			 * Choice the LexerParser according to type of the file
			 */
			if (!file.endsWith(".txt")) {
				lexParse = new BinaryFileLexParse();
			} else {
				lexParse = new CharacterFileLexParse();
			}

			interpreter = new Interpreter(file, lexParse, output, System.in);
			int status = interpreter.interpret();

			if (status == Interpreter.INTERPRET_SUCCEED) {
				System.err.println("INTERPRET SUCCEED.");
			} else if (status == Interpreter.INTERPRET_FAILED) {
				System.err.println("INTERPRET FAILED.");
			} else if (status == Interpreter.INTERPRET_STOPPED) {
				System.err.println("INTERPRET STOPPED.");
			}
		} catch (InvalidProgramException e) {
			switch (e.getTypeError()) {
			case InvalidProgramException.INVALID_CODES:
				System.err.print("Invalid codes, ");
				System.err.print("in file : " + e.getFileName()
						+ ", at line : " + e.getLineNbr() + ", (" + e.getLine()
						+ ")");
				break;

			default:
				System.err.print("Invalid file : " + e.getFileName());

			case InvalidProgramException.EMPTY_FILE:
				System.err.print(", empty file");
				break;
			case InvalidProgramException.FILE_ERROR:
				System.err.print(", check your file");
				break;
			case InvalidProgramException.INCOMPLETE_PROGRAM:
				System.err.print(", incomplete program");
				break;

			}
			System.err.println(".");
		}
	}

	public static void main(String[] args) {

		if (args.length < 0)
			System.err.println("Args error.");

		for (String file : args) {
			System.err.println("========== Running file " + file
					+ " ==========");
			long start = System.currentTimeMillis();
			runFile(file);
			long end = System.currentTimeMillis();

			System.err.println("Elapsed time to execute program : "
					+ ((end - start)) + " ms.");
		}
	}
}
