package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javafx.util.Pair;
import model.CityMap;
import model.Delivery;
import model.DeliveryRequest;
import model.Intersection;
import model.Journey;
import model.Round;
import model.RoundSet;

public class Algorithms {

	/**
	 * Computes the fastest path in the graph defined by map which starts from the intersection warehouse contained in the request, passes through each delivery points of the request and end in the warehouse and stores it in rounds.
	 * If the number of delivery men (nbDeliveryMan) is greater than 1, computes the same number of Rounds in the RoundSet, and every Rounds have the same number of delivery points +-1.
	 * At any points during the algorithm execution, the RoundSet rounds describes the best solution currently found.
	 * Despite of the duration of the RoundSet and the array Rounds in the RoundSet, nothing in the RoundSet is modified.
	 * 
	 * @param rounds
	 * 		Solution found by the algorithm
	 * @param map
	 * 		CityMap in which the path is found
	 * @param request
	 * 		Request describing from which point and to which points the path needs to pass through
	 * @param nbDeliveryMan
	 * 		The number of delivery men
	 */
	public static void solveTSP (RoundSet rounds, CityMap map , DeliveryRequest request, int nbDeliveryMan) throws ExceptionAlgo {
		
		Map<Long, Map<Long, Journey>> reducedMap;
		
		reducedMap = map.GetShortestJourneys(request);

		
		ArrayList<Delivery> visited = new ArrayList<Delivery>();
		visited.add(new Delivery(request.getWarehouse()));
		ArrayList<Delivery> cand = new ArrayList<Delivery> (request.getRequestDeliveries());

		rounds.setDuration(Double.MAX_VALUE);

		
		ArrayList<Integer> nbCandWhenChangingRound = new ArrayList<Integer>();
		int nbDelivery = request.getRequestDeliveries().size();
		int nbRounds = Math.min(nbDeliveryMan,  cand.size());
		for (int i = nbRounds; i > 0; i--) {
			nbDelivery -= nbDelivery/i;
			nbCandWhenChangingRound.add(nbDelivery);
		}
		
		branchAndBound(reducedMap, visited, cand, 0.0, rounds, nbCandWhenChangingRound, request.getWarehouse(), nbRounds - 1);

	}
	
	/**
	 * Update the current best solution of the TSP problem in method solveTSP with the newly found one in the RoundSet bestSolution.
	 * 
	 * @param reducedMap
	 * 		The cityMap in which the computation is done
	 * @param visited
	 * 		ArrayList of the currently already visited delivery points, ordered by passing date
	 * @param cand
	 * 		ArrayList of currently unvisited delivery points
	 * @param t
	 * 		The sum of all the rounds found for the new best solution
	 * @param bestSolution
	 * 		The current best solution, which is updated
	 * @param warehouse
	 * 		The warehouse from which the found path starts, and to which it ends.
	 */
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
	
	/**
	 * Creates an array of Integer representing the position of the remaining delivery points in the cand array sorted by ascending order by it's bound value
	 * (the estimated cost of the remaining best path starting from the delivery point, passing through every other delivery points, and ending
	 * in the warehouse)
	 * 
	 * @param bikersSpeed
	 * 		The biker speed
	 * @param reducedMap
	 * 		The shortest journeys matrix
	 * @param visited
	 * 		The already visited delivery points
	 * @param cand
	 * 		The remaining delivery points to visit
	 * @param t
	 * 		The duration of the whole partial path
	 * @param bestSolution
	 * 		The current best solution
	 * @param nbCandWhenChangingRound
	 * 		A list of depths (number of remaining points to visit) in which an artificial return to the warehouse is necessary (to create an another round, pass to an other delivery man)
	 * @param warehouse
	 * @param remainingReturnToWarehouse
	 * 		The remaining number of return to the warehouse (except the last one) to do
	 * @return the ordered list
	 */
	private static ArrayList<Pair<Double, Integer>> orderedByBoundCand (double bikersSpeed, Map<Long, Map<Long, Journey>> reducedMap , ArrayList<Delivery> visited, ArrayList<Delivery> cand, double t, 
			RoundSet bestSolution, ArrayList<Integer> nbCandWhenChangingRound, Intersection warehouse, int remainingReturnToWarehouse) {
		
		ArrayList<Pair<Double, Integer>> sorted = new ArrayList<Pair<Double, Integer>> ();
		
		boolean nextNewRound = (nbCandWhenChangingRound.indexOf(cand.size()-1) != -1);
		if (nextNewRound) remainingReturnToWarehouse--;
		for (int i = 0; i < cand.size(); i++) {
			Delivery d = cand.get(i);
			cand.remove(i);
			
			Double estimatedDuration;
			if (remainingReturnToWarehouse > 2) {
				estimatedDuration = new Double(boundMinSum(reducedMap, bikersSpeed, d, cand, warehouse, remainingReturnToWarehouse));
			} else {
				estimatedDuration = new Double(boundMunkres(reducedMap, bikersSpeed, d, cand, warehouse, remainingReturnToWarehouse));
			}
			
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
	
	/**
	 * The recursive method called by solveTSP to calculate the shortest path needed.
	 * @param reducedMap
	 * 		The cityMap in which the computation is done
	 * @param visited
	 * 		The already visited delivery points
	 * @param cand
	 * 		The remaining delivery points to visit
	 * @param t
	 * 		The duration of the whole partial path
	 * @param bestSolution
	 * 		The current best solution
	 *  @param nbCandWhenChangingRound
	 * 		A list of depths (number of remaining points to visit) in which an artificial return to the warehouse is necessary (to create an another round, pass to an other delivery man)
	 * @param warehouse
	 * @param remainingReturnToWarehouse
	 * 		The remaining number of return to the warehouse (except the last one) to do
	 */
	private static void branchAndBound (Map<Long, Map<Long, Journey>> reducedMap , ArrayList<Delivery> visited, ArrayList<Delivery> cand, double t, 
			RoundSet bestSolution, ArrayList<Integer> nbCandWhenChangingRound, Intersection warehouse, int remainingReturnToWarehouse) {
		
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

					branchAndBound(reducedMap, visited, cand, t + bikersSpeed * reducedMap.get(visited.get(visited.size()-2).getAdress().getId()).get(visited.get(visited.size()-1).getAdress().getId()).getLength() + visited.get(visited.size()-1).getDuration(), 
							bestSolution, nbCandWhenChangingRound, warehouse, remainingReturnToWarehouse);

					cand.add(j, visited.get(visited.size()-1));
					visited.remove(visited.size()-1);
				}
			}
		}
		if (newRound) 
			visited.remove(visited.size()-1);
	}
	
	/**
	 * The evaluation method, which computes a lower bound of the cost of the optimal path, passing through all the remaining delivery points and warehouse.
	 * @param reducedMap
	 * 		The cityMap in which the computation is done
	 * @param bikersSpeed
	 * 		The bikers' speed
	 * @param currentDelivery
	 * 		The last delivery point visited in the partial path computed during the branch and bound
	 * @param cand
	 * 		The remaining delivery points to visit
	 * @param warehouse
	 * @param remainingReturnToWarehouse
	 * 		The remaining number of return to the warehouse (except the last one) to do
	 * @return A lower bound of the cost of the optimal path
	 */
	private static double boundMinSum (Map<Long, Map<Long, Journey>> reducedMap,double bikersSpeed, Delivery currentDelivery, ArrayList<Delivery> cand, Intersection warehouse, int remainingReturnToWarehouse) {	
		
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
	
	/**
	 * The evaluation method, which computes a lower bound of the cost of the optimal path, passing through all the remaining delivery points and warehouse.
	 * This method uses an implementation of the Munkres' algorithm in O(N^3) complexity
	 * @param reducedMap
	 * 		The cityMap in which the computation is done
	 * @param bikersSpeed
	 * 		The bikers' speed
	 * @param currentDelivery
	 * 		The last delivery point visited in the partial path computed during the branch and bound
	 * @param cand
	 * 		The remaining delivery points to visit
	 * @param warehouse
	 * @param remainingReturnToWarehouse
	 * 		The remaining number of return to the warehouse (except the last one) to do
	 * @return A lower bound of the cost of the optimal path
	 */
	private static double boundMunkres (Map<Long, Map<Long, Journey>> reducedMap, double bikersSpeed, Delivery currentDelivery, 
			ArrayList<Delivery> cand, Intersection warehouse, int remainingReturnToWarehouse) {
		
		int nbVertices = cand.size() + remainingReturnToWarehouse + 1;
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
		
		augment(coupleX, coupleY, lx, ly, slack, slackx, 0, nbVertices, munkresCosts, xVertices, yVertices);
		
		double result = 0.0;
		
		for (int i = 0; i < nbVertices; i++) {
			
			result -= munkresCosts.get(xVertices.get(i)).get(yVertices.get(coupleX.get(i)));
			if (i < cand.size()) {
				result += cand.get(i).getDuration();
			} 
		}
		return result;
	}
	
	
	/**
	 * This is an open source implementation of the hungarian algorithm, which iteratively finds a perfect maximum matching in a complete bipartite graph in O(N^3) complexity.
	 * We consider X vertices as the left side vertices, and Y vertices as the right side vertices.
	 * For every arrays A (except munkresCosts), A[i] contains an information on the vertex of position i in the xVertices or yVertices arrays.
	 * @author Lucas GROLEAZ
	 * @param coupleX
	 * 		The array of matched Y vertices with the corresponding X vertex
	 * @param coupleY
	 * 		The array of matched X vertices with the corresponding Y vertex
	 * @param lx
	 * 		The array of label of X vertices
	 * @param ly
	 * 		The array of label of Y vertices
	 * @param slack
	 * 		The array containing at all time during the algorithm the minimum for all x in setS of (lx[x]+ly[y]-munkresCosts[xVertices[x]][yVertices[y]]) for each Y vertices y in setT
	 * @param slackx
	 * 		The array containing the X vertex minimizing the value of slack[y] for each Y vertices y
	 * @param maxMatch
	 * 		The size (number of edges) of the current matching
	 * @param nbVertices
	 * @param munkresCosts
	 * 		The matrix of duration of each shortest journeys from one delivery point/warehouse to another
	 * @param xVertices
	 * 		The array of ID of intersection of delivery points or warehouse for X vertices
	 * @param yVertices
	 * 		The array of ID of intersection of delivery points or warehouse for Y vertices
	 */
	private static void augment(ArrayList<Integer> coupleX, ArrayList<Integer> coupleY, ArrayList<Long> lx, ArrayList<Long> ly,
								ArrayList<Long> slack, ArrayList<Integer> slackx, int maxMatch, int nbVertices,
								Map<Long, Map<Long, Long>> munkresCosts, ArrayList<Long> xVertices, ArrayList<Long> yVertices) {

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
		
		for (x = 0; x < nbVertices; x++) {
			if (coupleX.get(x) == -1) {
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
	
		while (true) {
			while (!queue.isEmpty()) {
				x = queue.poll();
				for (y = 0; y < nbVertices; y++) {
					if ((munkresCosts.get(xVertices.get(x)).get(yVertices.get(y)) == lx.get(x) + ly.get(y)) && !setT.get(y)) {
						if (coupleY.get(y) == -1) {
							break;
						}
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
			
			for (y = 0; y < nbVertices; y++) {
				if (!setT.get(y) && (slack.get(y) == 0)) {
					if (coupleY.get(y) == -1) {
						x = slackx.get(y);
						break;
					} else {
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

			augment(coupleX, coupleY, lx, ly, slack, slackx, maxMatch, nbVertices, munkresCosts, xVertices, yVertices);
		}

	}

	private static void updateLabels (ArrayList<Long> lx, ArrayList<Long> ly, ArrayList<Boolean> setS, ArrayList<Boolean> setT, ArrayList<Long> slack, int nbVertices) {
		long delta = 100000000000L;
		for (int y = 0; y < nbVertices; y++) {
			if (!setT.get(y)) {
				delta = Math.min(delta, slack.get(y));
			}
		}
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
			}
		}
	}

	private static void addToTree(Map<Long, Map<Long, Long>> munkresCosts, ArrayList<Long> lx, ArrayList<Long> ly, ArrayList<Boolean> setS,
									ArrayList<Long> slack, ArrayList<Integer> slackx, ArrayList<Integer> prev,  ArrayList<Long> xVertices,
									ArrayList<Long> yVertices, int x, int prevx, int nbVertices) {
		setS.set(x, true);
		prev.set(x, prevx);
		for (int y = 0; y < nbVertices; y++) {
			if ((lx.get(x) + ly.get(y) - munkresCosts.get(xVertices.get(x)).get(yVertices.get(y))) < slack.get(y)) {
				slack.set(y, lx.get(x) + ly.get(y) - munkresCosts.get(xVertices.get(x)).get(yVertices.get(y)));
				slackx.set(y, x);
			}
		}
	}
	
}
