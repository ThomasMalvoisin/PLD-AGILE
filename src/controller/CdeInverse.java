package controller;


import model.Delivery;
import model.DeliveryRequest;
import model.RoundSet;

public class CdeInverse implements Command{
	Command cmd;
	
	public CdeInverse(Command cmd) {
		this.cmd = cmd;
	}
	
	@Override
	public void doCde() {
		cmd.undoCde();
	}
	
	@Override
	public void undoCde() {
		cmd.doCde();
	}
}