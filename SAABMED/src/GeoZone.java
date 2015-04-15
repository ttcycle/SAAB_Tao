

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;

import java.awt.geom.Point2D;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

public class GeoZone {

	private String geoZoneID;
	private String type;
	private double radius;
	private Position area;
	
	GeoZone()
	{
		this.setGeoZoneID("");
		this.setType("");
		this.setRadius(5000.0);
		this.setArea(Position.fromDegrees(0, 0));
	}
	
	GeoZone(String id, String type,Position area, double radius)
	{
		this.setGeoZoneID(id);
		this.setType(type);
		this.setRadius(radius);
		this.setArea(area);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGeoZoneID() {
		return geoZoneID;
	}

	public void setGeoZoneID(String geoZoneID) {
		this.geoZoneID = geoZoneID;
	}
	
	public double getradius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public Position getArea() {
		return area;
	}

	public void setArea(Position area) {
		this.area = area;
	}
	
}
