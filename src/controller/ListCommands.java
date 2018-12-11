package controller;

import java.util.LinkedList;

import algo.ExceptionAlgo;
import controller.Command;

public class ListCommands {
	private LinkedList<Command> list;
	private int indexList;
	
	public ListCommands(){
		indexList = -1;
		list = new LinkedList<Command>();
	}
	
	/**
	 * Ajout de la commande c a la list this
	 * @param c
	 * @throws ExceptionAlgo 
	 */
	public void ajoute(Command c) throws ExceptionAlgo{
			c.doCde();
			int i = indexList+1;
	        while(i<list.size()){
	        	System.out.println(i);
	        	list.remove(i);
	        }
	        indexList++;
	        list.add(indexList, c);	  
    }
	
	 /* Annule temporairement la derniere commande ajoutee (cette commande pourra etre remise dans la liste avec redo)
	 */
	public void undo(){
		if (indexList >= 0){
			Command cde = list.get(indexList);
			indexList--;
			cde.undoCde();
		}
	}
	
	/**
	 * Remet dans la liste la derniere commande annulee avec undo
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
	
	public void discard(){
		//int i = indiceCrt;
		while(indexList>-1) {
			undo();
		}
	}
	
	/**
	 * Supprime definitivement toutes les commandes de liste
	 */
	   public void reset(){
		   indexList = -1;
	        list.clear();  
	    }

}
