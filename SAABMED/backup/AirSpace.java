import java.util.ArrayList;

import gov.nasa.worldwind.geom.Position;

public class AirSpace {

	private int type;
	private ArrayList<Position> pos;
	private double upper, lower, radius;
	private String name;
	


	public AirSpace(int type, ArrayList<Position> pos, double upper, double lower) {
		this.type = type;
		this.pos = pos;
		this.upper = upper;
		this.lower = lower;
	}

	public AirSpace(int type, ArrayList<Position> pos, double radius, double upper, double lower) {
		this.type = type;
		this.pos = pos;
		this.radius = radius;
		this.upper = upper;
		this.lower = lower;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<Position> getPos() {
		return pos;
	}

	public void setPos(ArrayList<Position> pos) {
		this.pos = pos;
	}

	public double getUpper() {
		return upper;
	}

	public void setUpper(double upper) {
		this.upper = upper;
	}

	public double getLower() {
		return lower;
	}

	public void setLower(double lower) {
		this.lower = lower;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

}
