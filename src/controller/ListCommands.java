package controller;

import java.util.LinkedList;

import algo.ExceptionAlgo;
import controller.Command;

public class ListCommands {
	
	private LinkedList<Command> list;
	private int indexList;
	
	/**
	 * ListCommands controller
	 */
	public ListCommands(){
		indexList = -1;
		list = new LinkedList<Command>();
	}
	
	/**
	 * Add new command to list this 
	 * @param c command to add
	 * @throws ExceptionAlgo 
	 */
	public void ajoute(Command c) throws ExceptionAlgo{
			c.doCde();
			int i = indexList+1;
	        while(i<list.size()){
	        	list.remove(i);
	        }
	        indexList++;
	        list.add(indexList, c);	  
    }
	
	 /** 
	  * Cancel last command added 
	  * */
	public void undo(){
		if (indexList >= 0){
			Command cde = list.get(indexList);
			indexList--;
			cde.undoCde();
		}
	}
	
	/** Get back to the last command canceled
	 * 
	 */
	public void redo(){
		if (indexList < list.size()-1){
			indexList++;
			Command cde = list.get(indexList);
			try {
				cde.doCde();
			} catch (ExceptionAlgo e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Get back to first command
	 * 
	 * */
	public void discard(){
		while(indexList>-1) {
			undo();
		}
	}
	
	/**
	 * Delete all commands
	 */
	   public void reset(){
		   indexList = -1;
	        list.clear();  
	    }
}
