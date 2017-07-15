package upmc.machineuniverselle.exceptions;

/**
 * @author KASSAR Mohamed Tarek
 *
 */

public class InexistantResourceException extends UMException {

	private static final long serialVersionUID = -7918932081358339392L;

	private int tableId, plateauIndex;

	public InexistantResourceException(int tableId, int plateauIndex) {
		super();
		this.plateauIndex = plateauIndex;
		this.tableId = tableId;
	}

	
	public int getTableId() {
		return tableId;
	}

	public int getPlateauIndex() {
		return plateauIndex;
	}
	
}
