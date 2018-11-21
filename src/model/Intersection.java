package model;

public class Intersection {

	private float latitude;
	private float longitude;
	
	private long id;

	public Intersection(float latitude, float longitude, int id) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
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
