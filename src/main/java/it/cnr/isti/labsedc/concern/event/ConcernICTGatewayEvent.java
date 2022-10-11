package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernICTGatewayEvent<T> extends ConcernBaseEvent<T> {

	private static final long serialVersionUID = 1L;

	private String ICTMessageType;
	private String ICTMessageCategory;
//	private String ICTMessageTopologyID;


	public ConcernICTGatewayEvent(long timestamp, String senderID, String destinationID,
			String sessionID, String checksum, String name, T data, CepType type,
			boolean consumed, String extraProperty, String ICTMessageType, String ICTMessageCategory) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, type, consumed, extraProperty);
	
		this.setICTMessageCategory(ICTMessageCategory);
		this.setICTMessageType(ICTMessageType);
		
	}


	public String getICTMessageType() {
		return ICTMessageType;
	}


	public void setICTMessageType(String iCTMessageType) {
		ICTMessageType = iCTMessageType;
	}


	public String getICTMessageCategory() {
		return ICTMessageCategory;
	}


	public void setICTMessageCategory(String iCTMessageCategory) {
		ICTMessageCategory = iCTMessageCategory;
	}


//	public String getICTMessageTopologyID() {
//		return ICTMessageTopologyID;
//	}
//
//
//	public void setICTMessageTopologyID(String iCTMessageTopologyID) {
//		ICTMessageTopologyID = iCTMessageTopologyID;
//	}

}
