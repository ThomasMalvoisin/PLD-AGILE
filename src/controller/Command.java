package controller;

import algo.ExceptionAlgo;

public interface Command {
	
	/**
	 * Execute la commande this
	 * @throws ExceptionAlgo 
	 */
	void doCde() throws ExceptionAlgo;
	
	/**
	 * Execute la commande inverse a this
	 */
	void undoCde();
}
