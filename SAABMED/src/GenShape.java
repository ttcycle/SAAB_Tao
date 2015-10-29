import java.util.ArrayList;

import gov.nasa.worldwind.geom.Position;

public class GenShape {

	private int type;
	private ArrayList<Position> ASCoords;
	private double upper, lower, radius;
	private String name;
	private int Pos;
	


	public GenShape(int type, ArrayList<Position> pos, double upper, double lower) {
		this.name = "New Box Shape";
		this.type = type;
		this.ASCoords = pos;
		this.upper = upper;
		this.lower = lower;
	}

	public GenShape(int type, ArrayList<Position> pos, double radius, double upper, double lower, int Pos) {
		this.name = "New Cylinder Shape";
		this.type = type;
		this.ASCoords = pos;
		this.radius = radius;
		this.upper = upper;
		this.lower = lower;
		this.Pos = Pos;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<Position> getASCoords() {
		return ASCoords;
	}

	public void setASCoords(ArrayList<Position> pos) {
		this.ASCoords = pos;
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

	public int getPos() {
		return Pos;
	}

	public void setPos(int Pos) {
		this.Pos = Pos;
	}
	
}
