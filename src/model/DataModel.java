package model;

public class DataModel {
	public String caseID;
	public String crashType;
	public String configuration;
	public int numVehicles;
	public String[] vehicleNo;
	public String[] postedSpeedLimit;
	public String[] vehicleCrashType;
	public String[] total;
	public String[] longItudlnal;
	public String[] lateral;
	public String[] barrierEquivalent;
	public String imgPath;
	//public String movPath;
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(caseID);
		builder.append(" , ");
		builder.append(crashType);
		builder.append(" , ");
		builder.append(configuration);
	
		return builder.toString();
	}
}
