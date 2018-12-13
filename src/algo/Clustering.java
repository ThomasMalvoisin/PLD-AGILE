package algo;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import javafx.util.Pair;
import model.CityMap;
import model.Delivery;

public class Clustering {
@SuppressWarnings({ "unchecked", "rawtypes" })
public static ArrayList<ArrayList<Delivery>> cluster(CityMap map , ArrayList<Delivery> deliveries, int k){
		ArrayList<ArrayList<Delivery>> clusters = new ArrayList<ArrayList<Delivery>>();
		if(k == 1) {
			clusters.add(deliveries);
			return clusters ;
		}
		ArrayList<Pair<Double,Double>> centroids = new ArrayList<>(k) ;
		for(int  i = 0 ; i< k; i++) {
			centroids.add(getRandomCentroid(deliveries));
		}
		ArrayList<Pair<Double,Double>> prevCentroids = new ArrayList<>() ;

		while(!centroids.equals(prevCentroids)) {
			clusters = new ArrayList<ArrayList<Delivery>>();
			for(int  i = 0 ; i< k; i++) {
				clusters.add(new ArrayList<Delivery>());
			}
			for(Delivery delivery: deliveries) {
				int cl = getClosestCluster(centroids, delivery);
				clusters.get(cl).add(delivery);
			}
			prevCentroids = centroids ;
			centroids = computeCentroids(clusters);
		}
		
		clusters.sort((cl2, cl1) -> Integer.compare(cl1.size(),cl2.size()));
		for(int i = 0 ; i< clusters.size() ; i++) {
			Pair<Double,Double> centroid = prevCentroids.get(i);
			clusters.get(i).sort((dl2,dl1) -> (Double.compare(getDistance(centroid, new Pair(dl1.getAdress().getLatitude(),dl1.getAdress().getLongitude())),
					getDistance(centroid, new Pair(dl2.getAdress().getLatitude(),dl2.getAdress().getLongitude())))));
		}
		return clusters;
	}
	
	private static ArrayList<Pair<Double,Double>> computeCentroids(ArrayList<ArrayList<Delivery>> clusters){
		ArrayList<Pair<Double,Double>> res  = new ArrayList<>();
		for(ArrayList<Delivery> cluster : clusters) {
			res.add(computeClusterCentroid(cluster));
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private static Pair<Double,Double> computeClusterCentroid(ArrayList<Delivery> cluster){
		double x = 0;
	    double y = 0;
	    double z = 0;

	    for(Delivery delivery : cluster ) {
	        double lat = Math.toRadians(delivery.getAdress().getLatitude());
	        double lon = Math.toRadians(delivery.getAdress().getLongitude());
	        x += Math.cos(lat) * Math.cos(lon);
	        y += Math.cos(lat) * Math.sin(lon);
	        z += Math.sin(lat);
	    }
	    double k  = (double) cluster.size();
	    x = x / k;
	    y = y / k;
	    z = z / k;
	    
	    @SuppressWarnings("rawtypes")
		Pair p =  new Pair<Double,Double>(Math.toDegrees(Math.atan2(z, Math.sqrt(x * x + y * y))),Math.toDegrees(Math.atan2(y, x)));
	    return p;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static int getClosestCluster(ArrayList<Pair<Double,Double>> centroids, Delivery delivery) {
		int res = -1 ;
		double min = Double.MAX_VALUE;
		for(int i = 0 ; i<centroids.size();i++) {
			double dist  = getDistance(new Pair(delivery.getAdress().getLatitude(),delivery.getAdress().getLongitude()), centroids.get(i));
			if(dist < min) {
				min = dist;
				res = i ;
			}
		}
		return res;
	}
	
	private static double getDistance(Pair<Double,Double> p0, Pair<Double,Double> p) {
		double lat0 = p0.getKey();
		double lng0 = p0.getValue();
		double lat = p.getKey();
		double lng = p.getValue();
		double deglen =  110.25 ;
	    double x = lat - lat0 ;
	    double y = (lng - lng0)*Math.cos(Math.toRadians(lat0));
	    return deglen*Math.sqrt(x*x + y*y);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Pair<Double,Double> getRandomCentroid(ArrayList<Delivery> deliveries) {
		double latitudeMin = deliveries.stream()
				.mapToDouble(del -> del.getAdress().getLatitude())
				.min().orElseThrow(NoSuchElementException::new);
		double longitudeMin = deliveries.stream()
				.mapToDouble(del -> del.getAdress().getLongitude())
				.min().orElseThrow(NoSuchElementException::new);
		double latitudeMax = deliveries.stream()
				.mapToDouble(del -> del.getAdress().getLatitude())
				.max().orElseThrow(NoSuchElementException::new);
		double longitudeMax = deliveries.stream()
				.mapToDouble(del -> del.getAdress().getLongitude())
				.max().orElseThrow(NoSuchElementException::new);
		Random r = new Random();
		double randomLat = latitudeMin + (latitudeMax- latitudeMin) * r.nextDouble();
		double randomLng = longitudeMin + (longitudeMax - longitudeMin)* r.nextDouble();
		return new Pair(randomLat,randomLng);
	}
}
