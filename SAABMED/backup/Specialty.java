

import java.util.ArrayList;

public class Specialty {

	private ArrayList<String> type;
	private ArrayList<String> supplies;
	private ArrayList<String> casualties;
	private int capacity;
	private ArrayList<String> treatmentFacilities;
	private String comments;
	private String urgency;
	
	public Specialty()
	{
		this.setType(new ArrayList<String>());
		this.setSupplies(new ArrayList<String>());
		this.setCasualties(new ArrayList<String>());
		this.setCapacity(0);
		this.setTreatmentFacilities(new ArrayList<String>());
		this.setComments("");
		this.setUrgency("");
	}
	
	public Specialty(ArrayList<String> type, ArrayList<String> supplies, ArrayList<String> casualties, int capacity, ArrayList<String> treatmentFacilities, String comments, String urgency)
	{
		this.setType(type);
		this.setSupplies(supplies);
		this.setCasualties(casualties);
		this.setCapacity(capacity);
		this.setTreatmentFacilities(treatmentFacilities);
		this.setComments(comments);
		this.setUrgency(urgency);
	}

	public ArrayList<String> getType() {
		return type;
	}

	public void setType(ArrayList<String> type) {
		this.type = type;
	}

	public ArrayList<String> getSupplies() {
		return supplies;
	}

	public void setSupplies(ArrayList<String> supplies) {
		this.supplies = supplies;
	}

	public ArrayList<String> getCasualties() {
		return casualties;
	}

	public void setCasualties(ArrayList<String> casualties) {
		this.casualties = casualties;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public ArrayList<String> getTreatmentFacilities() {
		return treatmentFacilities;
	}

	public void setTreatmentFacilities(ArrayList<String> treatmentFacilities) {
		this.treatmentFacilities = treatmentFacilities;
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
	
	
}
