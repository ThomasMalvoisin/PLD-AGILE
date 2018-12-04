package algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javafx.util.Pair;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Journey;
import model.Round;
import model.RoundSet;
import model.Section;

public class Algorithms {

	public static RoundSet solveTSP (CityMap map , DeliveryRequest request, int nbDeliveryMan) {
		// Pour l'instant, nbDeliveryMan = 1 forcément
		Map<Long, Map<Long, Journey>> reducedMap = map.GetShortestJourneys(request);
		ArrayList<Delivery> visited = new ArrayList<Delivery>();
		visited.add(new Delivery(0, request.getWarehouse()));
		ArrayList<Delivery> cand = new ArrayList<Delivery> (request.getRequestDeliveries());
		System.out.println(cand.size());
		RoundSet rounds = new RoundSet();
		rounds.setDuration(Double.MAX_VALUE);	// Modifier branchAndBound pour construire un roundSet, et ajouter le numero de Round en cours de remplissage
		
		ArrayList<Integer> nbCandWhenChangingRound = new ArrayList<Integer>();
		int nbDelivery = request.getRequestDeliveries().size();
		for (int i = nbDeliveryMan; i > 0; i--) {
			nbDelivery -= nbDelivery/i;
			nbCandWhenChangingRound.add(nbDelivery);
		}
		for (Integer i : nbCandWhenChangingRound) System.out.print(i + "  ");
		System.out.println();
		
		branchAndBound(reducedMap, visited, cand, 0.0, rounds, nbCandWhenChangingRound, request.getWarehouse(), nbDeliveryMan-1);

		
		return rounds;
	}
	
	private static void branchAndBound (Map<Long, Map<Long, Journey>> reducedMap , ArrayList<Delivery> visited, ArrayList<Delivery> cand, double t, RoundSet bestSolution, ArrayList<Integer> nbCandWhenChangingRound, Intersection warehouse, int remainingReturnToWarehouse) {
		double bikersSpeed = 0.2399998;

		boolean newRound = (nbCandWhenChangingRound.indexOf(cand.size()) != -1);
		if (newRound) {
			Delivery returnToWarehouse = new Delivery(0, warehouse);
			t += bikersSpeed * reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(warehouse.getId()).getLength();
			visited.add(returnToWarehouse);
			remainingReturnToWarehouse--;
		}
		if (cand.size() == 1) {
			t += bikersSpeed * reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(cand.get(0).getAdress().getId()).getLength() + cand.get(0).getDuration();
			t += bikersSpeed * reducedMap.get(cand.get(0).getAdress().getId()).get(visited.get(0).getAdress().getId()).getLength();
			if (t < bestSolution.getDuration()) {
				visited.add(cand.get(0));
				bestSolution.setDuration(t);
				System.out.println("New bestSolution : ");
				
				Round currentRound = null;
				ArrayList<Journey> currentWay = null;
				ArrayList<Delivery> currentDeliveries = null;
				bestSolution.getRounds().clear();
				for(int i = 0; i < visited.size()-1; i++) {
					if (visited.get(i).getAdress() == warehouse) {
						if (i != 0) {
							currentRound.setJourneys(currentWay);
							currentRound.setDeliveries(currentDeliveries);
							bestSolution.getRounds().add(currentRound);
						}
						currentRound = new Round();
						currentWay = new ArrayList<Journey>();
						currentDeliveries = new ArrayList<Delivery>();
					}
					currentWay.add(reducedMap.get(visited.get(i).getAdress().getId()).get(visited.get(i+1).getAdress().getId()));
					currentDeliveries.add(visited.get(i));
					System.out.print(visited.get(i).getAdress().getId() + " ");
				}
				System.out.println(visited.get(visited.size()-1).getAdress().getId());
				currentWay.add(reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(warehouse.getId()));
				currentDeliveries.add(visited.get(visited.size()-1));
				currentRound.setJourneys(currentWay);
				currentRound.setDeliveries(currentDeliveries);
				bestSolution.getRounds().add(currentRound);
				visited.remove(visited.size()-1);
			}
		} else {
			
			ArrayList<Pair<Double, Integer>> sorted = new ArrayList<Pair<Double, Integer>> ();
			
			boolean nextNewRound = (nbCandWhenChangingRound.indexOf(cand.size()-1) != -1);
			if (nextNewRound) remainingReturnToWarehouse--;
			for (int i = 0; i < cand.size(); i++) {
				Delivery d = cand.get(i);
				cand.remove(i);

				Double estimatedDuration = new Double(bound(reducedMap,bikersSpeed,d, cand, warehouse, remainingReturnToWarehouse));

				sorted.add(new Pair<Double, Integer>(estimatedDuration, new Integer(i)));
				cand.add(i, d);
			}
			if (nextNewRound) remainingReturnToWarehouse++;
			
			Collections.sort(sorted, new Comparator<Pair<Double, Integer>>() {
			    public int compare(Pair<Double, Integer> i1, Pair<Double, Integer> i2) {
			    	Double comp = i1.getKey() - i2.getKey();
			    	if (comp < 0) return -1;
			    	else if (comp == 0) return 0;
			    	else return 1;
			    }
			});		
			
			for (int i = 0; i < sorted.size(); i++) {
				double addCost = 0.0;
				if (nextNewRound) {
					addCost = bikersSpeed * reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(warehouse.getId()).getLength();
					addCost += bikersSpeed * reducedMap.get(warehouse.getId()).get(cand.get(sorted.get(i).getValue()).getAdress().getId()).getLength() + cand.get(sorted.get(i).getValue()).getDuration();
				}
				else addCost = bikersSpeed * reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(cand.get(sorted.get(i).getValue()).getAdress().getId()).getLength() + cand.get(sorted.get(i).getValue()).getDuration();
				
				if (t + addCost + sorted.get(i).getKey() < bestSolution.getDuration()) {
					int j = sorted.get(i).getValue();
					visited.add(cand.get(j));
					cand.remove(j);

					branchAndBound(reducedMap, visited, cand, t + bikersSpeed * reducedMap.get(visited.get(visited.size()-2).getAdress().getId()).get(visited.get(visited.size()-1).getAdress().getId()).getLength() + visited.get(visited.size()-1).getDuration(), bestSolution, nbCandWhenChangingRound, warehouse, remainingReturnToWarehouse);

					cand.add(j, visited.get(visited.size()-1));
					visited.remove(visited.size()-1);
				}
			}
		}
		if (newRound) 
			visited.remove(visited.size()-1);
	}
	
	public static double bound (Map<Long, Map<Long, Journey>> reducedMap,double bikersSpeed, Delivery currentDelivery, ArrayList<Delivery> cand, Intersection warehouse, int remainingReturnToWarehouse) {	
		
		double result = 0.0;
		double minToNextCand = Double.MAX_VALUE, minToWarehouse = Double.MAX_VALUE;
		for (Delivery d : cand) {
			double toThisCand = reducedMap.get(currentDelivery.getAdress().getId()).get(d.getAdress().getId()).getLength();
			if (toThisCand < minToNextCand)
				minToNextCand = toThisCand;
			double minLength = Double.MAX_VALUE;
			for (Delivery e : cand) {
				if (e != d) {
					double currentCost = reducedMap.get(d.getAdress().getId()).get(e.getAdress().getId()).getLength();
					if (currentCost < minLength)
						minLength = currentCost;
				}
			}
			double toWarehouse = reducedMap.get(d.getAdress().getId()).get(warehouse.getId()).getLength();
			if (toWarehouse < minToWarehouse)
				minToWarehouse = toWarehouse;
			if (toWarehouse < minLength)
				minLength = toWarehouse;
			result += bikersSpeed * minLength;
			result += d.getDuration();
		}
		return result + bikersSpeed * (minToNextCand + remainingReturnToWarehouse * minToWarehouse);
	}
	
}
