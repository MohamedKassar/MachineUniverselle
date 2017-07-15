package upmc.machineuniverselle.lexparse;


import upmc.machineuniverselle.exceptions.InvalidProgramException;

public interface ILexParse {

	public static final int INVALID_PROGRAM = -1;

	public abstract int[] checkAndParse(String programFilePath)
			throws InvalidProgramException;

}