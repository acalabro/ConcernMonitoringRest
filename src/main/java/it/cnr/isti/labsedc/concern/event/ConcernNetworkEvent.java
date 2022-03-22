package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernNetworkEvent<T> extends ConcernAbstractEvent<T>{

	private static final long serialVersionUID = 1L;
	private int connectionsAmount;
	
	public ConcernNetworkEvent(long timestamp, String senderID, 
			String destinationID, String sessionID, 
			String checksum, String name, 
			T data, CepType type, int connectionsAmount) {
		
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, type);
		this.connectionsAmount = connectionsAmount;
	}
	
	public void setConnectionAmount(int connectionsAmount) {
		this.connectionsAmount = connectionsAmount;
	}
	
	public int getConnectionAmount() {
		return this.connectionsAmount;
	}
}
