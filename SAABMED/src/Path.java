

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Path {

	private String pathName;
	private ArrayList<Point2D> pathCoords;
	private double lat;
	private double lon;
	
	public Path()
	{
		this.setPathName("");
		this.setPathCoords(new ArrayList<Point2D>());
		this.setLat(0.00);
		this.setLon(0.00);
	}
	
	public Path(String pathName, ArrayList<Point2D> pathCoords, double lat, double lon)
	{
		this.setPathName(pathName);
		this.setPathCoords(pathCoords);
		this.setLat(lat);
		this.setLon(lon);
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public ArrayList<Point2D> getPathCoords() {
		return pathCoords;
	}

	public void setPathCoords(ArrayList<Point2D> pathCoords) {
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
}
