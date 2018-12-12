package model;

public class Intersection{

	private double latitude;
	private double longitude;
	
	private long id;
	
	

	/**
	 * Constructor. Create one intersection from latitude, longitude and id parameters
	 * @param latitude
	 * @param longitude
	 * @param id
	 */
	public Intersection(double latitude, double longitude, long id) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		
	}

	/**
	 * @return
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String	a ="Intersection " +id +".\n";
		return a;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
