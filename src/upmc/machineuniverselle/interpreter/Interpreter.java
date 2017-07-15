package upmc.machineuniverselle.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import upmc.machineuniverselle.exceptions.InexistantResourceException;
import upmc.machineuniverselle.exceptions.InvalidProgramException;
import upmc.machineuniverselle.exceptions.OperationNotSupportedException;
import upmc.machineuniverselle.exceptions.StopMachineException;
import upmc.machineuniverselle.lexparse.ILexParse;
import upmc.machineuniverselle.utilities.Utility;

/**
 * @author KASSAR Mohamed Tarek
 *
 */
public class Interpreter {
	public static long TWO_POW_32 = (long) Math.pow(2, 32);

	public static final int INTERPRET_SUCCEED = 0;
	public static final int INTERPRET_STOPPED = 1;
	public static final int INTERPRET_FAILED = 2;

	private final InputStream input;
	private final PrintStream output;

	
	private final LinkedList<Integer> freeTableIds = new LinkedList<>();
	private int currentTableId = 0;

	private final int[] registres = new int[8];
	private Map<Integer, int[]> tables = new HashMap<>();

	private int executionIndex;

	public Interpreter(String programFilePath, ILexParse lexPar,
			PrintStream output, InputStream input)
			throws InvalidProgramException {
		this.input = input;
		this.output = output;
		int[] program = lexPar.checkAndParse(programFilePath);
		tables.put(0, program);
	}

	public int interpret() {
		int status = -5;
		try {
			status = interpret(0);
		} catch (StopMachineException e) {
			Utility.printRepport("Interpret stopped.", executionIndex);
			return INTERPRET_STOPPED;

		} catch (OperationNotSupportedException e) {
			Utility.printRepport("Operation No." + e.getOperationNo()
					+ " not supported, interpret failed.", executionIndex);
			return INTERPRET_FAILED;

		} catch (InexistantResourceException e) {
			String message = "";
			if (e.getPlateauIndex() == -1) {
				message = "Table id : " + e.getTableId();
			} else {
				message = "In table id : " + e.getTableId() + " offset : "
						+ e.getPlateauIndex();
			}
			message += " not existant, interpret failed.";
			Utility.printRepport(message, executionIndex);
			return INTERPRET_FAILED;
		} catch (Exception e) {
			e.printStackTrace();
			Utility.printRepport("Internal error interpret failed, "
					+ "Contact developer at: mohamed.kassar@etu.upmc.fr .",
					executionIndex);
			return INTERPRET_FAILED;
		}
		return status;
	}

	private int tempTableId;
	private int[] tempTable;
	private int tempPlateauIndex;

	private int interpret(int startIndex) {

		InterpretInformation interpretInformation = new InterpretInformation();
		int[] programTable = tables.get(0);
		executionIndex = startIndex;

		while (executionIndex < programTable.length) {
			interpretInformation.setPlateau(programTable[executionIndex]);

			switch (interpretInformation.getOperationNo()) {

			case 0: {
				if (registres[interpretInformation.getRegCNo()] != 0) {
					registres[interpretInformation.getRegANo()] = registres[interpretInformation
							.getRegBNo()];
				}
				break;
			}
			case 1: {
				tempTableId = registres[interpretInformation.getRegBNo()];
				tempTable = tables.get(tempTableId);
				tempPlateauIndex = registres[interpretInformation.getRegCNo()];

				if (tempTable != null && tempTable.length > tempPlateauIndex) {
					registres[interpretInformation.getRegANo()] = tempTable[tempPlateauIndex];
				} else {
					throw new InexistantResourceException(tempTableId,
							tempPlateauIndex);
				}
				break;
			}
			case 2: {
				tempTableId = registres[interpretInformation.getRegANo()];
				tempTable = tables.get(tempTableId);
				tempPlateauIndex = registres[interpretInformation.getRegBNo()];
				if (tempTable != null && tempTable.length > tempPlateauIndex) {
					tempTable[tempPlateauIndex] = registres[interpretInformation
							.getRegCNo()];
				} else {
					throw new InexistantResourceException(tempTableId,
							tempPlateauIndex);
				}
				break;
			}

			case 3: {
				/*
				 * useless mod 2^32
				 */
				registres[interpretInformation.getRegANo()] = registres[interpretInformation
						.getRegBNo()]
						+ registres[interpretInformation.getRegCNo()];
				break;
			}
			case 4: {
				/*
				 * useless mod 2^32
				 */
				registres[interpretInformation.getRegANo()] = registres[interpretInformation
						.getRegBNo()]
						* registres[interpretInformation.getRegCNo()];
				break;
			}
			case 5: {
				registres[interpretInformation.getRegANo()] = (int) ((registres[interpretInformation
						.getRegBNo()] & 0xFFFFFFFFL) / (registres[interpretInformation
						.getRegCNo()] & 0xFFFFFFFFL));
				break;
			}
			case 6: {
				registres[interpretInformation.getRegANo()] = ~(registres[interpretInformation
						.getRegBNo()] & registres[interpretInformation
						.getRegCNo()]);
				break;
			}

			case 8: {
				tempTableId = (freeTableIds.isEmpty() ? ++currentTableId
						: freeTableIds.remove(0));
				tables.put(tempTableId,
						new int[registres[interpretInformation.getRegCNo()]]);
				registres[interpretInformation.getRegBNo()] = tempTableId;
				break;
			}
			case 9: {
				tempTableId = registres[interpretInformation.getRegCNo()];
				if (tables.containsKey(tempTableId)) {
					tables.remove(tempTableId);
					freeTableIds.add(tempTableId);
				} else {
					throw new InexistantResourceException(tempTableId, -1);
				}
				break;
			}
			case 10: {
				if (registres[interpretInformation.getRegCNo()] >= 0
						&& registres[interpretInformation.getRegCNo()] <= 255) {
					char c = (char) registres[interpretInformation.getRegCNo()];
					output.print(c);
				}
				break;
			}
			case 11: {
				try {
					char c = (char) input.read();
					registres[interpretInformation.getRegCNo()] = c;
				} catch (IOException e) {
					registres[interpretInformation.getRegCNo()] = 0xFFFFFFFF;
				}
				break;
			}
			case 12: {
				tempTableId = registres[interpretInformation.getRegBNo()];
				tempPlateauIndex = registres[interpretInformation.getRegCNo()];
				if (tempTableId == 0) {
					executionIndex = tempPlateauIndex;
					continue;
				} else {
					int[] table = tables.get(tempTableId);
					if (table != null && table.length > tempPlateauIndex) {
						executionIndex = tempPlateauIndex;
						programTable = table.clone();
						tables.put(0, programTable);
						continue;
					} else {
						throw new InexistantResourceException(tempTableId,
								tempPlateauIndex);
					}
				}
			}
			case 13: {
				registres[interpretInformation.getOrthoRegANo()] = interpretInformation
						.getOrthoValue();
				break;
			}
			
			case 7:
				throw new StopMachineException();
			default:
				throw new OperationNotSupportedException(
						interpretInformation.getOperationNo());
			}
			
			executionIndex++;
		}
		
		Utility.printRepport("Interpret success.", -1);
		return INTERPRET_SUCCEED;
	}

}
