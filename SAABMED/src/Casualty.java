

public class Casualty {

	private String casualtyID;
	private String wounds;
	private String nationality;
	
	public String toString()
	{
		return this.casualtyID + ", " + this.wounds + ", " + this.nationality;
	}
	
	public Casualty()
	{
		this.setCasualtyID("");
		this.setWounds("");
		this.setNationality("");
	}
	
	public Casualty(String casualtyID, String wounds, String nationality)
	{
		this.setCasualtyID(casualtyID);
		this.setWounds(wounds);
		this.setNationality(nationality);
	}

	public String getCasualtyID() {
		return casualtyID;
	}

	public void setCasualtyID(String casualtyID) {
		this.casualtyID = casualtyID;
	}

	public String getWounds() {
		return wounds;
	}

	public void setWounds(String wounds) {
		this.wounds = wounds;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
}
