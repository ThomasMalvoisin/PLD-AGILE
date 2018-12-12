package controller;

import algo.ExceptionAlgo;

public interface Command {
	
	/**
	 * Execute command this
	 * @throws ExceptionAlgo when method add new delivery throws an exception
	 */
	void doCde() throws ExceptionAlgo;
	
	/**
	 * Execute reverse command
	 */
	void undoCde();
}
