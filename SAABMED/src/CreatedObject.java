

public class CreatedObject {
	private String objectType;
	private AO ao;
	private GeoZone geoZone;
	private GenericSymbology genericSymbology;
	private MecialSymbology medicalSymbology;
	private Route route;
	private POI poi;
	private AirSpace as; 
	private GenShape GS;
	
	public CreatedObject()
	{
		this.setObjectType("");
		this.setAo(null);
		this.setGeoZone(null);
		this.setGenericSymbology(null);
		this.setMedicalSymbology(null);
		this.setRoute(null);
		this.setPoi(null);		
		this.setAs(null);
	}
	
	public CreatedObject(String objectType, Object object)
	{
		this.setObjectType(objectType);
		if(objectType == "AO")
		{
			this.setAo((AO)object);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi(null);
			this.setAs(null);
		}
		else if(objectType == "GeoZone")
		{
			this.setAo(null);
			this.setGeoZone((GeoZone)object);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi(null);
			this.setAs(null);
		}
		else if(objectType == "GenericSymbology")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology((GenericSymbology)object);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi(null);
			this.setAs(null);
		}
		else if(objectType == "MedicalSymbology")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology((MecialSymbology)object);;
			this.setRoute(null);
			this.setPoi(null);
			this.setAs(null);
		}
		else if(objectType == "Route")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute((Route)object);;
			this.setPoi(null);
			this.setAs(null);
		}
		else if(objectType == "POI")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi((POI)object);;
			this.setAs(null);
		}
		else if(objectType == "AirSpace")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi(null);
			this.setAs((AirSpace)object);
		}
		else if(objectType == "GenShape")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi(null);
			this.setGS((GenShape)object);
		}
	}
	
	public boolean hasGeoZone() {
		if (this.geoZone.getGeoZoneID() == "NULL") {
			return false;
		} else {
			return true;
		}
	}
	
	public String toString()
	{
		String objectID = "";
		if(getObjectType() == "AO")
		{
			objectID = this.getAo().getAoID();
		}
		else if(getObjectType() == "GeoZone")
		{
			objectID = this.getGeoZone().getGeoZoneID();
		}
		else if(getObjectType() == "GenericSymbology")
		{
			objectID = this.getGenericSymbology().getsID();
		}
		else if(getObjectType() == "MedicalSymbology")
		{
			objectID = this.getMedicalSymbology().getSymbol().getsID();
		}
		else if(getObjectType() == "Route")
		{
			objectID = this.getRoute().getPathName();
		}
		else if(getObjectType() == "POI")
		{
			objectID = this.getPoi().getPoiID();
		}
		else if(getObjectType() == "AirSpace") {
			objectID = this.getAs().getName();
		}
		else if(getObjectType() == "GenShape") {
			objectID = this.getGS().getName();
		}
		return objectID + " (" + getObjectType() + ")";
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public AO getAo() {
		return ao;
	}

	public void setAo(AO ao) {
		this.ao = ao;
	}

	public GeoZone getGeoZone() {
		return geoZone;
	}

	public void setGeoZone(GeoZone geoZone) {
		this.geoZone = geoZone;
	}

	public GenericSymbology getGenericSymbology() {
		return genericSymbology;
	}

	public void setGenericSymbology(GenericSymbology genericSymbology) {
		this.genericSymbology = genericSymbology;
	}

	public MecialSymbology getMedicalSymbology() {
		return medicalSymbology;
	}

	public void setMedicalSymbology(MecialSymbology medicalSymbology) {
		this.medicalSymbology = medicalSymbology;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public POI getPoi() {
		return poi;
	}

	public void setPoi(POI poi) {
		this.poi = poi;
	}
	
	public void setAs(AirSpace as) {
		this.as = as;
	}
	
	public GenShape getGS() {
		return GS;
	}

	public void setGS(GenShape gS) {
		GS = gS;
	}

	public AirSpace getAs() {
		return as;
	}
	
}
