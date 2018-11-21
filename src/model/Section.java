package model;

public class Section {

	private Intersection origin;
	private Intersection destination;
	private String name;
	private float length;
	
	public Section(Intersection origin, Intersection destination, String name, float length) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.name = name;
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public Intersection getOrigin() {
		return origin;
	}

	public Intersection getDestination() {
		return destination;
	}

	public float getLength() {
		return length;
	}

	@Override
	public boolean equals(Object obj) {
		
		Section section = (Section)obj;
		
		if(section.getDestination() == this.destination) {
			if(section.getOrigin() == this.origin) {
				if(section.getLength() == this.length) {
					if(section.getName() == this.name) {
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
