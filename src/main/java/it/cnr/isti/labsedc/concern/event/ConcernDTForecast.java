package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernDTForecast<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private String trustedIntervalInSeconds;
	private String forecastedProbeName;
	
	public ConcernDTForecast(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T forecast,
			CepType type,
			boolean consumed,
			String trustedIntervalInSeconds,
			String forecastedProbeName) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, forecast, type, consumed);
		this.trustedIntervalInSeconds = trustedIntervalInSeconds;	
		this.forecastedProbeName = forecastedProbeName;
	}

	public void setTrustedInterval(String trustedIntervalInSeconds) {
		this.trustedIntervalInSeconds = trustedIntervalInSeconds;
	}
	
	public String getTrustedInterval() {
		return this.trustedIntervalInSeconds;
	}

	public void setForecastedProbeName(String forecastedProbeName) {
		this.forecastedProbeName = forecastedProbeName;
	}
	
	public String getForecastedProbeName() {
		return this.forecastedProbeName;
	}
}
