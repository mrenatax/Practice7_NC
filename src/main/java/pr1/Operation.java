package pr1;
/**
* Operation on two arguments.
*/
public class Operation {
	/**
	* Create new operation using two numbers.
	*/
	public Operation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	* @return result of operation
	*/
	public int getResult() {
		return x * y;
	}
	private int x;
	private int y;
} 