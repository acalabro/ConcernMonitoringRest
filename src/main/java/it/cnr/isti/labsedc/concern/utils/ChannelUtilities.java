package it.cnr.isti.labsedc.concern.utils;

import java.util.Vector;

public class ChannelUtilities {

	public static Vector<String> loadServiceChannels() {

		Vector<String> serviceListenerChannels = new Vector<String>();

		serviceListenerChannels.add("ServiceChannel-ONE");
		serviceListenerChannels.add("ServiceChannel-TWO");
		serviceListenerChannels.add("ServiceChannel-THREE");

		return serviceListenerChannels;
	}
	
	public static Vector<String> loadEventChannels() {

		Vector<String> eventListenerChannels = new Vector<String>();

		eventListenerChannels.add("EventChannel-ONE");
		eventListenerChannels.add("EventChannel-TWO");
		eventListenerChannels.add("EventChannel-THREE");
		eventListenerChannels.add("EventChannel-FOUR");

		return eventListenerChannels;
	}
}
