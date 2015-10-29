

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Polyline;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Route {

	private String pathName;
	private ArrayList<Position> pathCoords;
	private double lat;
	private double lon;
	private double length;
	private int listPos;
	public int getListPos() {
		return listPos;
	}

	public void setListPos(int listPos) {
		this.listPos = listPos;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(List<Position> positions, double elevation) {
		
		this.polyline = new Polyline(positions,elevation);
	}

	private int Pos;
	private Polyline polyline;
	
	public Route()
	{
		this.setPathName("");
		this.setPathCoords(new ArrayList<Position>());
		this.setLat(0.00);
		this.setLon(0.00);
		this.setLength(0.00);
	}

	public int getPos() {
		return Pos;
	}

	public void setPos(int pos) {
		Pos = pos;
	}

	public Route(Polyline pathCoords) {
		ArrayList<Position> newCoords = (ArrayList<Position>) pathCoords.getPositions();
		this.pathCoords = newCoords;
	}
	
	public Route(String pathName, ArrayList<Position> pathCoords, double lat, double lon,int Pos, Polyline polyline)
	{
		this.setPathName(pathName);
		this.setPathCoords(pathCoords);
		this.setLat(lat);
		this.setLon(lon);
		this.setPos(Pos);
		this.setPolyline(polyline);
		//this.setLength(length);
	}

	private void setPolyline(Polyline polyline2) {
		// TODO Auto-generated method stub
		
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
