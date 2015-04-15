

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AO {

	private String aoID;
	private double lat;
	private double lon;
	private ArrayList<Point2D> aoCoords;
	private String opName;
	private String classification;
	private String Comments;
	
	public AO()
	{
		this.setAoID("");	
		this.setLat(0.00);
		this.setLon(0.00);
		this.setAoCoords(new ArrayList<Point2D>());
		this.setOpName("");
		this.setClassification("");
		this.setComments("");
	}
	
	public AO(String aoID, double lat, double lon, ArrayList<Point2D> aoCoords, String opName, String classification, String comments)
	{
		this.setAoID(aoID);	
		this.setLat(lat);
		this.setLon(lon);
		this.setAoCoords(aoCoords);
		this.setOpName(opName);
		this.setClassification(classification);
		this.setComments(comments);
	}

	public String getAoID() {
		return aoID;
	}

	public void setAoID(String aoID) {
		this.aoID = aoID;
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

	public ArrayList<Point2D> getAoCoords() {
		return aoCoords;
	}

	public void setAoCoords(ArrayList<Point2D> aoCoords) {
		this.aoCoords = aoCoords;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}
	
}
