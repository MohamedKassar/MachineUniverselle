package upmc.machineuniverselle.interpreter;

/**
 * @author KASSAR Mohamed Tarek
 *
 */

public class InterpretInformation {
	
	private int plateau;
	
	public void setPlateau(int plateau) {
		this.plateau = plateau;
	}

	public int getRegANo() {
		return (plateau >> 6) & 0x7;
	}

	public int getRegBNo() {
		return (plateau >> 3) & 0x7;
	}

	public int getRegCNo() {
		return plateau & 0x7;
	}

	public int getOperationNo() {
		return (plateau >> 28) & 0xF;
	}

	public int getOrthoRegANo() {
		return (plateau >> 25) & 0x7;
	}
	
	public int getOrthoValue() {
		return plateau & 0x1FFFFFF;
	}
	
}
