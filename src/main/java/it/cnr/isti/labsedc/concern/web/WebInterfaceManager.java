package it.cnr.isti.labsedc.concern.web;

import it.cnr.isti.labsedc.concern.ConcernApp;

public class WebInterfaceManager {

	public WebInterfaceManager() {
		ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
	}
}
