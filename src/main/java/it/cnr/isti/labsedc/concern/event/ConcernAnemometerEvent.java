package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernAnemometerEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = -8368776464020831391L;

	private double latitude; 
	private double longitude;
	private int WindAngle;
	private int WindStrength;
	private int GustAngle;
	private int GustStrength;

	
	public ConcernAnemometerEvent(long timestamp, String senderID, String destinationID, String sessionID, String checksum,
			String name, T data, CepType type, boolean consumed, int WindAngle, int WindStrength, int GustAngle, int GustStrength,
			double latitude, double longitude) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, type, consumed);
		this.setWindAngle(WindAngle);
		this.setWindStrength(WindStrength);
		this.setGustAngle(GustAngle);
		this.setGustStrength(GustStrength);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}
	
	private void setWindAngle(int windAngle) {
		WindAngle = windAngle;
	}
	
	public int getWindAngle() {
		return WindAngle;
	}

	private void setWindStrength(int windStrength) {
		WindStrength = windStrength;
	}
	
	public int getWindStrenght() {
		return WindStrength;
	}

	private void setGustAngle(int gustAngle) {
		GustAngle = gustAngle;
	}
	
	public int getGustAngle() {
		return GustAngle ;
	}

	private void setGustStrength(int gustStrength) {
		GustStrength = gustStrength;
	}
	
	public int getGustStrength() {
		return GustStrength ;
	}

	public double getLatitude() {
		return latitude;
	}

	private void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
