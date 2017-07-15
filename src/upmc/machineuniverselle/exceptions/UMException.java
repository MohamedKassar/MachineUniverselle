package upmc.machineuniverselle.exceptions;

/**
 * @author KASSAR Mohamed Tarek
 *
 */

public abstract class UMException extends RuntimeException {

	private static final long serialVersionUID = 7057337391915370415L;

	public UMException() {
		/*
		 * to make the exception ignore the trace
		 */
		super(null, null, false, false);
	}

}
