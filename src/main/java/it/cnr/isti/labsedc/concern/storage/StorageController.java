package it.cnr.isti.labsedc.concern.storage;

import it.cnr.isti.labsedc.concern.event.Event;
public interface StorageController {

	public boolean connectToDB();
	public boolean disconnectFromDB();
	public boolean saveMessage(Event<?> message);
}
