package upmc.machineuniverselle.lexparse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import upmc.machineuniverselle.exceptions.InvalidProgramException;

/**
 * @author KASSAR Mohamed Tarek
 *
 * Treat binary files
 */
public class BinaryFileLexParse implements ILexParse {

	@Override
	public int[] checkAndParse(String programFilePath) throws InvalidProgramException {
		Path path = Paths.get(programFilePath);
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new InvalidProgramException(
					InvalidProgramException.FILE_ERROR, programFilePath);
		}

		if (bytes.length % 4 != 0) {
			throw new InvalidProgramException(
					InvalidProgramException.INCOMPLETE_PROGRAM, programFilePath);
		}

		int program[] = new int[bytes.length / 4];
		for (int i = 0; i < program.length; i++) {
			int j = i * 4;
			program[i] = ((bytes[j] & 0xFF) << 24)
					| ((bytes[j + 1] & 0xFF) << 16)
					| ((bytes[j + 2] & 0xFF) << 8)
					| ((bytes[j + 3] & 0xFF) << 0);
		}

		return program;
	}

}
