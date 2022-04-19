package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernCmdVelEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;

	private float angularx=0f;
	private float angulary=0f;
	private float angularz=0f;
	
	private float linearx=0f;
	private float lineary=0f;
	private float linearz=0f;
	
	public ConcernCmdVelEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T msgData,
			CepType type,
			boolean consumed,
			float angularx, float angulary, float angularz, float linearx, float lineary, float linearz) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, msgData, type, consumed);
		
		this.angularx = angularx;
		this.angulary = angulary;
		this.angularz = angularz;
		
		this.linearx = linearx;
		this.lineary = lineary;
		this.linearz = linearz;
	}

	public float getAngularx() {
		return angularx;
	}

	public void setAngularx(float angularx) {
		this.angularx = angularx;
	}

	public float getAngulary() {
		return angulary;
	}

	public void setAngulary(float angulary) {
		this.angulary = angulary;
	}

	public float getAngularz() {
		return angularz;
	}

	public void setAngularz(float angularz) {
		this.angularz = angularz;
	}

	public float getLinearx() {
		return linearx;
	}

	public void setLinearx(float linearx) {
		this.linearx = linearx;
	}

	public float getLineary() {
		return lineary;
	}

	public void setLineary(float lineary) {
		this.lineary = lineary;
	}

	public float getLinearz() {
		return linearz;
	}

	public void setLinearz(float linearz) {
		this.linearz = linearz;
	}
}
