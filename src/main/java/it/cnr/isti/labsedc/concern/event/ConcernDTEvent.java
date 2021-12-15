package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernDTEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private ConcernDTEvent<?> previous;
	
	public ConcernDTEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T data,
			CepType type,
			ConcernDTEvent<?> previous) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, type);
		this.previous = previous;		
	}

	public void setPrevious(ConcernDTEvent<?> previous) {
		this.previous = previous;
	}
	
	public ConcernDTEvent<?> getPrevious() {
		return this.previous;
	}

}
