package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernDTForecast<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private String trustedIntervalInSeconds;
	private String forecastedProbeName;
	private String forecastedProperty;
	private String thresholdValue;
	
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
			String trustedIntervalInSeconds, //7
			String forecastedProbeName, //SUA Probe STUB
			String forecastedProperty, //{LATENCY, AVERAGE, SEQUENCE}
			String thresholdValue) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, forecast, type, consumed);
		this.trustedIntervalInSeconds = trustedIntervalInSeconds;	
		this.thresholdValue = thresholdValue; 
		this.forecastedProbeName = forecastedProbeName;
		this.forecastedProperty = forecastedProperty;
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
	
	public void setForecastedProperty(String forecastedProperty) {
		this.forecastedProperty = forecastedProperty;
	}
	
	public String getForecastedProperty() {
		return this.forecastedProperty;
	}

	public void setThresholdValue(String thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	
	public String getThresholdValue() {
		return this.thresholdValue;
	}

	
}
