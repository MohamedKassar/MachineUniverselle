package upmc.machineuniverselle.utilities;

public class Utility {
	
	/*
	 * Useful for printing errors in a log file
	 */
	public static void printRepport(String message, long executionIndex){	
		System.err.println();
		String toPrint = "";
		if(executionIndex != -1)
			toPrint = "At execution index : " + executionIndex + ".\n";
		
		System.err.println(toPrint + message);
	}
}
