package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernBaseEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	
	private String property;
	
	public ConcernBaseEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T data,
			CepType type,
			boolean consumed,
			String property) {
		
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, type, consumed);
		
		this.property = property;	
	}

	public void setProperty(String aProperty) {
		this.property = aProperty;
	}
	
	public String getProperty() {
		return this.property;
	}
}
