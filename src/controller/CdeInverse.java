package controller;


import algo.ExceptionAlgo;
import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;

public class CdeInverse implements Command{
	Command cmd;
	
	/** Create reverse command to command cmd
	 * @param cmd
	 */
	public CdeInverse(Command cmd) {
		this.cmd = cmd;
	}
	
	@Override
	public void doCde() {
		cmd.undoCde();
	}
	
	@Override
	public void undoCde() {
		try {
			cmd.doCde();
		} catch (ExceptionAlgo e) {
			e.printStackTrace();
		}
	}
}
