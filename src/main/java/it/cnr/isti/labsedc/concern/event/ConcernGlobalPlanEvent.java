package it.cnr.isti.labsedc.concern.event;

import it.cnr.isti.labsedc.concern.cep.CepType;

public class ConcernGlobalPlanEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;

	private String frame_id ="";
	
	private float posesPoseOrientationW = 0f;
	private float posesPoseOrientationX = 0f;
	private float posesPoseOrientationY = 0f;
	private float posesPoseOrientationZ = 0f;
	
	private float posesPositionX = 0f;
	private float posesPositionY = 0f;
	private float posesPositionZ = 0f;
	
	public ConcernGlobalPlanEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T msgData,
			CepType type,
			boolean consumed,
			String frame_id,
			float posesPoseOrientationW, float posesPoseOrientationX, float posesPoseOrientationY, float posesPoseOrientationZ,
			float posesPositionX, float posesPositionY,float posesPositionZ) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, msgData, type, consumed);
		
		this.frame_id = frame_id;
		
		this.posesPoseOrientationW = posesPoseOrientationW;
		this.posesPoseOrientationX = posesPoseOrientationX;
		this.posesPoseOrientationY = posesPoseOrientationY;
		this.posesPoseOrientationZ = posesPoseOrientationZ;
		
		this.posesPositionX = posesPositionX;
		this.posesPositionY = posesPositionY;
		this.posesPositionZ = posesPositionZ;
	}

	public String getFrame_id() {
		return frame_id;
	}

	public void setFrame_id(String frame_id) {
		this.frame_id = frame_id;
	}

	public float getPosesPoseOrientationW() {
		return posesPoseOrientationW;
	}

	public void setPosesPoseOrientationW(float posesPoseOrientationW) {
		this.posesPoseOrientationW = posesPoseOrientationW;
	}

	public float getPosesPoseOrientationX() {
		return posesPoseOrientationX;
	}

	public void setPosesPoseOrientationX(float posesPoseOrientationX) {
		this.posesPoseOrientationX = posesPoseOrientationX;
	}

	public float getPosesPoseOrientationY() {
		return posesPoseOrientationY;
	}

	public void setPosesPoseOrientationY(float posesPoseOrientationY) {
		this.posesPoseOrientationY = posesPoseOrientationY;
	}

	public float getPosesPoseOrientationZ() {
		return posesPoseOrientationZ;
	}

	public void setPosesPoseOrientationZ(float posesPoseOrientationZ) {
		this.posesPoseOrientationZ = posesPoseOrientationZ;
	}

	public float getPosesPositionX() {
		return posesPositionX;
	}

	public void setPosesPositionX(float posesPositionX) {
		this.posesPositionX = posesPositionX;
	}

	public float getPosesPositionY() {
		return posesPositionY;
	}

	public void setPosesPositionY(float posesPositionY) {
		this.posesPositionY = posesPositionY;
	}

	public float getPosesPositionZ() {
		return posesPositionZ;
	}

	public void setPosesPositionZ(float posesPositionZ) {
		this.posesPositionZ = posesPositionZ;
	}

	
}
