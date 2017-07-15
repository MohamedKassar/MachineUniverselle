package upmc.machineuniverselle.exceptions;

/**
 * @author KASSAR Mohamed Tarek
 *
 */

public class OperationNotSupportedException extends UMException {
	private static final long serialVersionUID = -3820630440550644029L;
	private final int operationNo;
	
	public OperationNotSupportedException(int operationNo) {
		this.operationNo = operationNo;
	}
	
	public int getOperationNo() {
		return operationNo;
	}
}
