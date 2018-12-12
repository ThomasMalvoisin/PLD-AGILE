package model;

public class Section{

	private Intersection origin;
	private Intersection destination;
	private String name;
	private double length;
	
	/**
	 * @param origin
	 * @param destination
	 * @param name
	 * @param length
	 */
	public Section(Intersection origin, Intersection destination, String name, double length) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.name = name;
		this.length = length;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public Intersection getOrigin() {
		return origin;
	}

	/**
	 * @return
	 */
	public Intersection getDestination() {
		return destination;
	}

	/**
	 * @return
	 */
	public double getLength() {
		return length;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String a = "- Take "+name +" for "+Math.round(length)+ " meters. \n";
		return a;
	}
	
	/**
	 * Return the inverse section
	 * Switch the origin and the destination
	 * @return
	 */
	public Section getInverse() {
		   return new Section(destination, origin, name, length);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		Section section = (Section)obj;
		
		if(section.getDestination().equals(this.destination)) {
			if(section.getOrigin().equals(this.origin)) {
				if(section.getLength() == this.length) {
					if(section.getName().equals(this.name)) {
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
		}else {
			return false;
		}
	}		
}
