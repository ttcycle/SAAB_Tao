

import java.util.ArrayList;

public class POI {

	private String poiID;
	private String poiName;
	private String description;
	private String enemyTac;
	private int casualtyNumber;
	private String priority;
	private String mobility;
	private ArrayList<Casualty> casualties;
	private String urgency;
	private String comments;
	
	public POI()
	{
		this.setPoiID("");
		this.setPoiName("");
		this.setDescription("");
		this.setEnemyTac("");
		this.setCasualtyNumber(0);
		this.setPriority("");
		this.setMobility("");
		this.setCasualties(new ArrayList<Casualty>());
		this.setUrgency("");
		this.setComments("");
	}
	
	public POI(String poiID, String poiName, String description, String enemyTac, int casualtyNumber, String priority, String mobility, ArrayList<Casualty> casualties, String urgency, String comments)
	{
		this.setPoiID(poiID);
		this.setPoiName(poiName);
		this.setDescription(description);
		this.setEnemyTac(enemyTac);
		this.setCasualtyNumber(casualtyNumber);
		this.setPriority(priority);
		this.setMobility(mobility);
		this.setCasualties(casualties);
		this.setUrgency(urgency);
		this.setComments(comments);
	}

	public String getPoiID() {
		return poiID;
	}

	public void setPoiID(String poiID) {
		this.poiID = poiID;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnemyTac() {
		return enemyTac;
	}

	public void setEnemyTac(String enemyTac) {
		this.enemyTac = enemyTac;
	}

	public int getCasualtyNumber() {
		return casualtyNumber;
	}

	public void setCasualtyNumber(int casualtyNumber) {
		this.casualtyNumber = casualtyNumber;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getMobility() {
		return mobility;
	}

	public void setMobility(String mobility) {
		this.mobility = mobility;
	}

	public ArrayList<Casualty> getCasualties() {
		return casualties;
	}

	public void setCasualties(ArrayList<Casualty> casualties) {
		this.casualties = casualties;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
