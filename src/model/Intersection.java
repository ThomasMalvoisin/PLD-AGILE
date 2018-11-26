package model;

public class Intersection{

	private double latitude;
	private double longitude;
	
	private long id;
	
	public static double latitudeMin = Double.MAX_VALUE;
	public static double longitudeMin = Double.MAX_VALUE;
	public static double latitudeMax = Double.MIN_VALUE;
	public static double longitudeMax = Double.MIN_VALUE;

	public Intersection(double latitude, double longitude, long id) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		
		if(latitude > latitudeMax) {
			latitudeMax = latitude;
		}
		if(latitude < latitudeMin) {
			latitudeMin = latitude;
		}
		if(longitude > longitudeMax) {
			longitudeMax = longitude;
		}
		if(longitude < longitudeMin) {
			longitudeMin = longitude;
		}
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public long getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		
		Intersection intersection = (Intersection)obj;
		
		if(this.id == intersection.getId()) {
			if(this.latitude == intersection.getLatitude()) {
				if(this.longitude == intersection.getLongitude()) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
	}	
}
