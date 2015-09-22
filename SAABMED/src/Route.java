

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Polyline;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Route {

	private String pathName;
	private ArrayList<Position> pathCoords;
	private double lat;
	private double lon;
	private double length;
	
	public Route()
	{
		this.setPathName("");
		this.setPathCoords(new ArrayList<Position>());
		this.setLat(0.00);
		this.setLon(0.00);
		this.setLength(0.00);
	}

	public Route(Polyline pathCoords) {
		ArrayList<Position> newCoords = (ArrayList<Position>) pathCoords.getPositions();
		this.pathCoords = newCoords;
	}
	
	public Route(String pathName, ArrayList<Position> pathCoords, double lat, double lon)
	{
		this.setPathName(pathName);
		this.setPathCoords(pathCoords);
		this.setLat(lat);
		this.setLon(lon);
		//this.setLength(length);
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public ArrayList<Position> getPathCoords() {
		return pathCoords;
	}

	public void setPathCoords(ArrayList<Position> pathCoords) {
		this.pathCoords = pathCoords;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	private void setLength(double length) {
		this.length = length;
		
	}
}
