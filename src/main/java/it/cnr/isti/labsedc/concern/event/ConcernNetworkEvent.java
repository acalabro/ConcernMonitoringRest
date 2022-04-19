package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernNetworkEvent<T> extends ConcernAbstractEvent<T>{

	private static final long serialVersionUID = 1L;

	public ConcernNetworkEvent(long timestamp, String senderID, 
			String destinationID, String sessionID, 
			String checksum, String name, 
			T data, CepType type,
			boolean consumed) {
		
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, type, consumed);
	}
		
	public double getConnectionAmount() {
		return Double.parseDouble(getData().toString());
	}
}
