

import java.util.ArrayList;

public class MecialSymbology {

	private ArrayList<Specialty> specialities;
	private GenericSymbology symbol;
	private String comments;
	private String urgency;
	private int Pos;
	
	public int getPos() {
		return Pos;
	}

	public void setPos(int pos) {
		Pos = pos;
	}

	public MecialSymbology()
	{
		this.setSpecialities(new ArrayList<Specialty>());
		this.setSymbol(new GenericSymbology());
		this.setComments("");
		this.setUrgency("");
	}
	
	public MecialSymbology(ArrayList<Specialty> specialities, GenericSymbology symbol, String comments, String urgency, int Pos)
	{
		this.setSpecialities(specialities);
		this.setSymbol(symbol);
		this.setComments(comments);
		this.setUrgency(urgency);
		this.setPos(Pos);
	}

	public ArrayList<Specialty> getSpecialities() {
		return specialities;
	}

	public void setSpecialities(ArrayList<Specialty> specialities) {
		this.specialities = specialities;
	}

	public GenericSymbology getSymbol() {
		return symbol;
	}

	public void setSymbol(GenericSymbology symbol) {
		this.symbol = symbol;
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
