package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernProbeEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private String relayStatus;
	
	public ConcernProbeEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T data,
			CepType cepType,
			boolean consumed,
			String relayStatus) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, cepType, consumed);
		this.relayStatus = relayStatus;	
	}

	public void setRelayStatus(String relayStatus) {
		this.relayStatus = relayStatus;
	}
	
	public String getRelayStstus() {
		return this.relayStatus;
	}
}
