package model;

public class Intersection {

	private double latitude;
	private double longitude;
	
	private long id;

	public Intersection(double latitude, double longitude, long id) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
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
