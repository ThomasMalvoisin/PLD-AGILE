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

	public static void solveTSP (RoundSet rounds, CityMap map , DeliveryRequest request, int nbDeliveryMan) throws ExceptionAlgo {
		Clustering.cluster(map, request.getRequestDeliveries(), nbDeliveryMan);
		System.out.println("finished clustering");
		// Pour l'instant, nbDeliveryMan = 1 forcément
		
		Map<Long, Map<Long, Journey>> reducedMap;
		try {
			reducedMap = map.GetShortestJourneys(request);
		} catch (ExceptionAlgo e) {
			throw new ExceptionAlgo(e.getMessage());
		}
		
		ArrayList<Delivery> visited = new ArrayList<Delivery>();
		visited.add(new Delivery(request.getWarehouse()));
		ArrayList<Delivery> cand = new ArrayList<Delivery> (request.getRequestDeliveries());

		//System.out.println(cand.size());
		rounds.setDuration(Double.MAX_VALUE);

		
		ArrayList<Integer> nbCandWhenChangingRound = new ArrayList<Integer>();
		int nbDelivery = request.getRequestDeliveries().size();
		for (int i = nbDeliveryMan; i > 0; i--) {
			nbDelivery -= nbDelivery/i;
			nbCandWhenChangingRound.add(nbDelivery);
		}
		//for (Integer i : nbCandWhenChangingRound) System.out.print(i + "  ");
		//System.out.println();
		
		branchAndBound(reducedMap, visited, cand, 0.0, rounds, nbCandWhenChangingRound, request.getWarehouse(), Math.min(nbDeliveryMan-1, cand.size()-1));

	}
	
	private static void updateBestSolution (Map<Long, Map<Long, Journey>> reducedMap, ArrayList<Delivery> visited, ArrayList<Delivery> cand, double t, RoundSet bestSolution, Intersection warehouse) {
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
	
	private static ArrayList<Pair<Double, Integer>> orderedByBoundCand (double bikersSpeed, Map<Long, Map<Long, Journey>> reducedMap , ArrayList<Delivery> visited, ArrayList<Delivery> cand, double t, RoundSet bestSolution, ArrayList<Integer> nbCandWhenChangingRound, Intersection warehouse, int remainingReturnToWarehouse) {
		ArrayList<Pair<Double, Integer>> sorted = new ArrayList<Pair<Double, Integer>> ();
		
		boolean nextNewRound = (nbCandWhenChangingRound.indexOf(cand.size()-1) != -1);
		if (nextNewRound) remainingReturnToWarehouse--;
		for (int i = 0; i < cand.size(); i++) {
			Delivery d = cand.get(i);
			cand.remove(i);

			Double estimatedDuration = new Double(bound2(reducedMap, bikersSpeed, d, cand, warehouse, remainingReturnToWarehouse));

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
		
		return sorted;
	}
	
	private static void branchAndBound (Map<Long, Map<Long, Journey>> reducedMap , ArrayList<Delivery> visited, ArrayList<Delivery> cand, double t, RoundSet bestSolution, ArrayList<Integer> nbCandWhenChangingRound, Intersection warehouse, int remainingReturnToWarehouse) {
		double bikersSpeed = 0.2399998;

		boolean newRound = (nbCandWhenChangingRound.indexOf(cand.size()) != -1);
		if (newRound) {
			Delivery returnToWarehouse = new Delivery(warehouse);
			t += bikersSpeed * reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(warehouse.getId()).getLength();
			visited.add(returnToWarehouse);
			remainingReturnToWarehouse--;
		}
		if (cand.size() == 1) {
			t += bikersSpeed * reducedMap.get(visited.get(visited.size()-1).getAdress().getId()).get(cand.get(0).getAdress().getId()).getLength() + cand.get(0).getDuration();
			t += bikersSpeed * reducedMap.get(cand.get(0).getAdress().getId()).get(visited.get(0).getAdress().getId()).getLength();
			if (t < bestSolution.getDuration()) {
				updateBestSolution(reducedMap, visited, cand, t, bestSolution, warehouse);
			}
		} else {
			
			ArrayList<Pair<Double, Integer>> sorted = orderedByBoundCand(bikersSpeed, reducedMap, visited, cand, t, bestSolution, nbCandWhenChangingRound, warehouse, remainingReturnToWarehouse);
			
			boolean nextNewRound = (nbCandWhenChangingRound.indexOf(cand.size()-1) != -1);
			
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
	
	public static double bound1 (Map<Long, Map<Long, Journey>> reducedMap,double bikersSpeed, Delivery currentDelivery, ArrayList<Delivery> cand, Intersection warehouse, int remainingReturnToWarehouse) {	
		
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
	
	public static double bound2 (Map<Long, Map<Long, Journey>> reducedMap, double bikersSpeed, Delivery currentDelivery, ArrayList<Delivery> cand, Intersection warehouse, int remainingReturnToWarehouse) {
		int nbVertices = cand.size() + remainingReturnToWarehouse + 1;
		//System.out.println("Debut du bound " + currentDelivery.getAdress().getId() + " " + remainingReturnToWarehouse);
		ArrayList<Long> xVertices = new ArrayList<Long>(), yVertices = new ArrayList<Long>();
		for (int i = 0; i < cand.size(); i++) {
			xVertices.add(cand.get(i).getAdress().getId());
			yVertices.add(cand.get(i).getAdress().getId());
		}
		xVertices.add(currentDelivery.getAdress().getId());
		yVertices.add(warehouse.getId());
		for (int i = 0; i < remainingReturnToWarehouse; i++) {
			xVertices.add(warehouse.getId());
			yVertices.add(warehouse.getId());
		}
		
		Map<Long, Map<Long, Long>> munkresCosts = new HashMap<Long, Map<Long, Long>>();
		for (int i = 0; i <= cand.size(); i++) {
			Map<Long, Long> costsFromI = new HashMap<Long, Long>();
			for (int j = 0; j <= cand.size(); j++) {
				if (i == j) {
					costsFromI.put(yVertices.get(j), -1000000000L);
				} else {
					costsFromI.put(yVertices.get(j), (long)(-Math.floor(bikersSpeed * reducedMap.get(xVertices.get(i)).get(yVertices.get(j)).getLength())));
				}
			}
			munkresCosts.put(xVertices.get(i), costsFromI);
		}
		if (remainingReturnToWarehouse > 0) {
			Map<Long, Long> costsFromWarehouse = new HashMap<Long, Long>();
			for (int j = 0; j < cand.size(); j++) {
				costsFromWarehouse.put(yVertices.get(j), (long)(-Math.floor(bikersSpeed * reducedMap.get(warehouse.getId()).get(yVertices.get(j)).getLength())));
			}
			costsFromWarehouse.put(warehouse.getId(), -1000000000L);
			munkresCosts.put(warehouse.getId(), costsFromWarehouse);
		}
		ArrayList<Integer> coupleX = new ArrayList<Integer>();
		ArrayList<Integer> coupleY = new ArrayList<Integer>();
		ArrayList<Long> lx = new ArrayList<Long>();
		ArrayList<Long> ly = new ArrayList<Long>();
		ArrayList<Long> slack = new ArrayList<Long>();
		ArrayList<Integer> slackx = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> saveCouples = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<ArrayList<Long>>> saveLx = new ArrayList<ArrayList<ArrayList<Long>>>();
		ArrayList<ArrayList<ArrayList<Long>>> saveLy = new ArrayList<ArrayList<ArrayList<Long>>>();
		for (int i = 0; i < nbVertices; i++) {
			coupleX.add(-1);
			coupleY.add(-1);
			lx.add(Long.MIN_VALUE);
			ly.add(0L);
			for (int j = 0; j < nbVertices; j++) {
				lx.set(i, Math.max(lx.get(i), munkresCosts.get(xVertices.get(i)).get(yVertices.get(j))));
			}
			slack.add(0L);
			slackx.add(-1);
		}
		
		augment(coupleX, coupleY, lx, ly, slack, slackx, 0, nbVertices, munkresCosts, xVertices, yVertices, saveCouples, saveLx, saveLy);
		
		double result = 0.0;
		
		for (int i = 0; i < nbVertices; i++) {
			
			result -= munkresCosts.get(xVertices.get(i)).get(yVertices.get(coupleX.get(i)));
			if (i < cand.size()) {
				result += cand.get(i).getDuration();
			} 
		}
		if (result > 100000.0) {
			double res = 0.0;
			for (int i = 0; i < nbVertices; i++) res -= munkresCosts.get(xVertices.get(i)).get(yVertices.get(coupleX.get(i)));
			System.out.println(res);
			System.out.println("\nGALEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERE\nGALEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERE\nGALEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEERE");
			System.out.println("Gros probleme : " + currentDelivery.getAdress().getId());
			for (int m = 0; m < xVertices.size(); m++) {
				for (int j = 0; j < xVertices.size(); j++) {
					System.out.print(munkresCosts.get(xVertices.get(m)).get(yVertices.get(j)) + " ");
				}
				System.out.println();
			}
			System.out.println();
			for (int j = 0; j < saveCouples.size(); j++) {
				
				ArrayList<ArrayList<Long>> labelX = saveLx.get(j);
				ArrayList<ArrayList<Long>> labelY = saveLy.get(j);
				for (int k = 0; k < labelX.size(); k++) {
					System.out.println("Labelx : " + labelX.get(k));
					System.out.println("Labely : " + labelY.get(k));
					System.out.println();
				}
				System.out.println();
				System.out.print("CouplageX : ");
				System.out.println(saveCouples.get(j));
			}
			
			System.out.println();
		}
		return result;
	}
	
	private static void augment(ArrayList<Integer> coupleX, ArrayList<Integer> coupleY, ArrayList<Long> lx, ArrayList<Long> ly,
								ArrayList<Long> slack, ArrayList<Integer> slackx, int maxMatch, int nbVertices,
								Map<Long, Map<Long, Long>> munkresCosts, ArrayList<Long> xVertices, ArrayList<Long> yVertices, 
								ArrayList<ArrayList<Integer>> saveCouples, ArrayList<ArrayList<ArrayList<Long>>> saveLx, ArrayList<ArrayList<ArrayList<Long>>> saveLy) {
		
		saveLx.add(new ArrayList<ArrayList<Long>>());
		saveLy.add(new ArrayList<ArrayList<Long>>());
		
		if (maxMatch == nbVertices) {
			return;
		}
		
		int root = -1;
		int x;
		int y;

		LinkedList<Integer> queue = new LinkedList<Integer>();
		ArrayList<Boolean> setS = new ArrayList<Boolean>();
		ArrayList<Boolean> setT = new ArrayList<Boolean>();
		ArrayList<Integer> prev = new ArrayList<Integer>();
		
		for (int i = 0; i < nbVertices; i++) {
			setS.add(false);
			setT.add(false);
			prev.add(-1);
		}
		
		//System.out.println("Current maxMatch : " + maxMatch);
		for (x = 0; x < nbVertices; x++) {
			if (coupleX.get(x) == -1) {
//				System.out.println("Depart de la file prevu sur : "+x);
				queue.add(x);
				root = x;
				prev.set(x, -2);
				setS.set(x, true);
				break;
			}
		}
		
		for (y = 0; y < nbVertices; y++) {
			slack.set(y, lx.get(root) + ly.get(y) - munkresCosts.get(xVertices.get(root)).get(yVertices.get(y)));
			slackx.set(y, root);
		}
//		System.out.println("Slack : "+slack);
		
		while (true) {
			while (!queue.isEmpty()) {
				x = queue.poll();
//				System.out.println("Parcours a partir de : " + x);
				for (y = 0; y < nbVertices; y++) {
					if ((munkresCosts.get(xVertices.get(x)).get(yVertices.get(y)) == lx.get(x) + ly.get(y)) && !setT.get(y)) {
//						System.out.println("Rentrée dans T de : " + y);
						if (coupleY.get(y) == -1) {
//							System.out.println("Pas couplé, donc nouveau chemin augmentant");
							break;
						}
//						System.out.println("Deja couplé, on l'ajoute dans l'arbre");
						setT.set(y, true);
						queue.add(coupleY.get(y));
						addToTree(munkresCosts, lx, ly, setS, slack, slackx, prev, xVertices, yVertices, coupleY.get(y), x, nbVertices);
					}
				}
				if (y < nbVertices) {
					break;
				}
			}
			if (y < nbVertices) {
				break;
			}

			updateLabels(lx, ly, setS, setT, slack, nbVertices);
//			System.out.println("Slack : "+slack);
			saveLx.get(saveLx.size()-1).add(new ArrayList<Long>(lx));
			saveLy.get(saveLy.size()-1).add(new ArrayList<Long>(ly));
			
			for (y = 0; y < nbVertices; y++) {
				if (!setT.get(y) && (slack.get(y) == 0)) {
//					System.out.println("Rentrée dans T de apres update: " + y);
					if (coupleY.get(y) == -1) {
//						System.out.println("Pas couplé, donc nouveau chemin augmentant");
						x = slackx.get(y);
						break;
					} else {
//						System.out.println("Deja couplé, on l'ajoute dans l'arbre");
						setT.set(y, true);
						if (!setS.get(coupleY.get(y))) {
							queue.add(coupleY.get(y));
							addToTree(munkresCosts, lx, ly, setS, slack, slackx, prev, xVertices, yVertices, coupleY.get(y), slackx.get(y), nbVertices);
						}
					}
				}
			}
			if (y < nbVertices) {
				break;
			}
		}

		if (y < nbVertices) {
			maxMatch++;
			for (int cx = x, cy = y, ty; cx != -2; cx = prev.get(cx), cy = ty)
			{
				ty = coupleX.get(cx);
				coupleY.set(cy, cx);
				coupleX.set(cx, cy);
			}

			saveCouples.add(new ArrayList<Integer>(coupleX));
//			System.out.println("Labelx : "+lx);
//			System.out.println("Labely : "+ly);
//			System.out.println("CoupleX : "+coupleX);
			augment(coupleX, coupleY, lx, ly, slack, slackx, maxMatch, nbVertices, munkresCosts, xVertices, yVertices, saveCouples, saveLx, saveLy);
		}

	}

	private static void updateLabels (ArrayList<Long> lx, ArrayList<Long> ly, ArrayList<Boolean> setS, ArrayList<Boolean> setT, ArrayList<Long> slack, int nbVertices) {
//		System.out.println("Update des label");
		long delta = 100000000000L;
		for (int y = 0; y < nbVertices; y++) {
			if (!setT.get(y)) {
				delta = Math.min(delta, slack.get(y));
			}
		}
//		System.out.println("Delta vaut : "+ delta);
		for (int x = 0; x < nbVertices; x++) {
			if (setS.get(x)) {
				lx.set(x, lx.get(x) - delta);
			}
		}
		for (int y = 0; y < nbVertices; y++) {
			if (setT.get(y)) {
				ly.set(y, ly.get(y) + delta);
			} else {
				slack.set(y, slack.get(y) - delta);
//				System.out.println("Modification de la slack de : " + y + "; " + slack.get(y));
			}
		}
	}

	private static void addToTree(Map<Long, Map<Long, Long>> munkresCosts, ArrayList<Long> lx, ArrayList<Long> ly, ArrayList<Boolean> setS,
									ArrayList<Long> slack, ArrayList<Integer> slackx, ArrayList<Integer> prev,  ArrayList<Long> xVertices,
									ArrayList<Long> yVertices, int x, int prevx, int nbVertices) {
		setS.set(x, true);
		prev.set(x, prevx);
		for (int y = 0; y < nbVertices; y++) {
//			System.out.println("Label de "+x+" : "+lx.get(x));
//			System.out.println("Label de "+y+" : "+ly.get(y));
//			System.out.println("Coute de x a y :"+munkresCosts.get(xVertices.get(x)).get(yVertices.get(y)));
			if ((lx.get(x) + ly.get(y) - munkresCosts.get(xVertices.get(x)).get(yVertices.get(y))) < slack.get(y)) {
//				System.out.println("Nouvelle valeur de slack pour : " + y + "; " + (lx.get(x) + ly.get(y) - munkresCosts.get(xVertices.get(x)).get(yVertices.get(y))));
				slack.set(y, lx.get(x) + ly.get(y) - munkresCosts.get(xVertices.get(x)).get(yVertices.get(y)));
				slackx.set(y, x);
			}
		}
	}
	
}
