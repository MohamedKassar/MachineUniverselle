package upmc.machineuniverselle.lexparse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import upmc.machineuniverselle.exceptions.InvalidProgramException;

/*
 * Treat text files
 */
public class CharacterFileLexParse implements ILexParse {

	/*
	 * TODO : code to optimize
	 */
	@Override
	public int[] checkAndParse(String programFilePath) throws InvalidProgramException {
		File programFile = new File(programFilePath);
		int[] program = new int[checkProgram(programFile)];
		int isPlateau = 0;
		int index = 0;
		String line, plateau = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(programFile));
			while ((line = br.readLine()) != null) {
				if (line.equals("")) {
					continue;
				}
				isPlateau++;
				plateau += line;
				if (isPlateau == 4) {
					program[index] = (int) Long.parseLong(plateau, 2);
					isPlateau = 0;
					plateau = "";
					index++;
				}
			}
			br.close();
		} catch (IOException e) {
			/*
			 * never reached
			 */
		}
		return program;
	}


	private int checkProgram(File programFile) throws InvalidProgramException {
		String line;
		double plateauNumber = 0;
		long lineNbr = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(programFile));
			while ((line = br.readLine()) != null) {
				lineNbr++;
				if (line.equals("")) {
					continue;
				}
				if (line.length() != 8
						|| line.replaceAll("[01]", "").length() != 0) {
					br.close();
					throw new InvalidProgramException(
							InvalidProgramException.INVALID_CODES, programFile.getName(),
							lineNbr, line.substring(0, 5) + "...");
				}
				plateauNumber += 0.25;
			}
			br.close();
		} catch (IOException e) {
			throw new InvalidProgramException(
					InvalidProgramException.FILE_ERROR, programFile.getName());
		}

		if (plateauNumber != 0 && plateauNumber == (int) plateauNumber)
			return (int) plateauNumber;
		if (plateauNumber == 0)
			throw new InvalidProgramException(
					InvalidProgramException.EMPTY_FILE, programFile.getName());

		throw new InvalidProgramException(
				InvalidProgramException.INCOMPLETE_PROGRAM, programFile.getName());
	}

}
