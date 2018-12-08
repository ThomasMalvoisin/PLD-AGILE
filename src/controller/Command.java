package controller;

public interface Command {
	
	/**
	 * Execute la commande this
	 */
	void doCde();
	
	/**
	 * Execute la commande inverse a this
	 */
	void undoCde();
}
