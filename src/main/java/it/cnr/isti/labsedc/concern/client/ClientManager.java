package it.cnr.isti.labsedc.concern.client;

import it.cnr.isti.labsedc.concern.ConcernApp;

public class ClientManager {

	public ClientManager() {
		ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
	}

}
