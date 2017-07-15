package upmc.machineuniverselle.exceptions;

/**
 * @author KASSAR Mohamed Tarek
 *
 */

public class InvalidProgramException extends UMException {
	private static final long serialVersionUID = 8974955599455820439L;

	public final static int INVALID_CODES = 0;
	public final static int FILE_ERROR = 1;
	public final static int INCOMPLETE_PROGRAM = 2;
	public final static int EMPTY_FILE = 3;

	private final int typeError;
	private final String programFilePath;
	private final long lineNbr;
	private final String line; 
	
	public InvalidProgramException(int typeError, String file) {
		this(typeError, file, -1, null);
	}

	
	public InvalidProgramException(int typeError, String file, long lineNbr, String line) {
		super();
		this.typeError = typeError;
		this.programFilePath = file;
		this.lineNbr = lineNbr;
		this.line = line;
	}


	public int getTypeError() {
		return typeError;
	}


	public String getFileName() {
		return programFilePath;
	}


	public String getLine() {
		return line;
	}
	
	public long getLineNbr() {
		return lineNbr;
	}
	
	
}
