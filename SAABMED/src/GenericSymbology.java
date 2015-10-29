import gov.nasa.worldwind.geom.Position;



public class GenericSymbology {
	
	private String sID;
	private String type;
	private String codeName;
	private double size;
	private double opacity;
	private boolean visible;
	private String comments;
	private String urgency;
	private Position position;
	private GeoZone geoZone;
	private String OPName;
	private int Pos;
	
	public GenericSymbology()
	{
		this.setsID("");
		this.setType("");
		this.setCodeName("");
		this.setSize(0.00);
		this.setOpacity(0.00);
		this.setVisible(true);
		this.setComments("");
		this.setUrgency("");
		this.setPosition(Position.fromDegrees(0, 0));
		this.setGeoZone(new GeoZone());
		this.setOPName("");
	}
	
	public GenericSymbology(String sID, String type, String codeName, double size, double opacity, boolean visible, String comments, String urgency, Position position, GeoZone geoZone, int Pos)
	{
		this.setsID(sID);
		this.setType(type);
		this.setCodeName(codeName);
		this.setSize(size);
		this.setOpacity(opacity);
		this.setVisible(visible);
		this.setComments(comments);
		this.setUrgency(urgency);
		this.setPosition(position);
		this.setGeoZone(geoZone);
		this.setOPName("");
		this.setPos(Pos);
	}

	public int getPos() {
		return Pos;
	}

	public void setPos(int pos) {
		Pos = pos;
	}

	public String getOPName() {
		return OPName;
	}
	
	public void setOPName(String newName) {
		this.OPName = newName;
	}
	
	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public GeoZone getGeoZone() {
		return geoZone;
	}

	public void setGeoZone(GeoZone geoZone) {
		this.geoZone = geoZone;
	}
	
}
