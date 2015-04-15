

public class CreatedObject {
	private String objectType;
	private AO ao;
	private GeoZone geoZone;
	private GenericSymbology genericSymbology;
	private MecialSymbology medicalSymbology;
	private Route route;
	private POI poi;
	 
	public CreatedObject()
	{
		this.setObjectType("");
		this.setAo(null);
		this.setGeoZone(null);
		this.setGenericSymbology(null);
		this.setMedicalSymbology(null);
		this.setRoute(null);
		this.setPoi(null);		
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
		}
		else if(objectType == "GeoZone")
		{
			this.setAo(null);
			this.setGeoZone((GeoZone)object);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi(null);
		}
		else if(objectType == "GenericSymbology")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology((GenericSymbology)object);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi(null);
		}
		else if(objectType == "MedicalSymbology")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology((MecialSymbology)object);;
			this.setRoute(null);
			this.setPoi(null);
		}
		else if(objectType == "Route")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute((Route)object);;
			this.setPoi(null);
		}
		else if(objectType == "POI")
		{
			this.setAo(null);
			this.setGeoZone(null);
			this.setGenericSymbology(null);
			this.setMedicalSymbology(null);
			this.setRoute(null);
			this.setPoi((POI)object);;
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
}
