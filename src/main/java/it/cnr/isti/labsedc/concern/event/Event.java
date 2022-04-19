package it.cnr.isti.labsedc.concern.event;

public interface Event <T>{

	public long getTimestamp();
	public void setTimestamp(long timestamp);
	public String getSenderID();
	public void setSenderID(String senderID);
	public String getDestinationID();
	public void setDestinationID(String destinationID);
	public String getName();
	public void setName(String name);
	public T getData();
	public void setData(T t);
	public boolean getConsumed();
	public void setConsumed(boolean consumed);
}
