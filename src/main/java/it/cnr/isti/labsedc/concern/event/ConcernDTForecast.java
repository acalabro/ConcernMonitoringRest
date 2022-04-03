package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernDTForecast<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private String trustedIntervalInSeconds;
	
	public ConcernDTForecast(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T forecast,
			CepType type,
			String trustedIntervalInSeconds) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, forecast, type);
		this.trustedIntervalInSeconds = trustedIntervalInSeconds;		
	}

	public void setTrustedInterval(String trustedIntervalInSeconds) {
		this.trustedIntervalInSeconds = trustedIntervalInSeconds;
	}
	
	public String getTrustedInterval() {
		return this.trustedIntervalInSeconds;
	}

}
