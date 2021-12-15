package it.cnr.isti.labsedc.concern.mediator;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;
import it.cnr.isti.labsedc.concern.utils.ConcernMQTTCallBack;

public class Mediator extends Thread {
	
	static MqttClient sender;
	static MqttClient listener;
	static String MQTTEntrypoint;
	static MqttMessage message;
	static String MQTTListenerChannel;
	
	public Mediator(String MQTTEntrypoint, String MQTTListenerChannel) {
		Mediator.MQTTEntrypoint = MQTTEntrypoint;
		Mediator.MQTTListenerChannel = MQTTListenerChannel;
	}

	public void run() {
		try {
			sender = new MqttClient(Mediator.MQTTEntrypoint, MqttClient.generateClientId());
			sender.connect();
			listener = new MqttClient(Mediator.MQTTEntrypoint, MqttClient.generateClientId());
			listener.setCallback(new ConcernMQTTCallBack());
			listener.connect();
			listener.subscribe(MQTTListenerChannel);
			
			
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}		
	
	public static void prepareMessage(String msg) {
		message = new MqttMessage();

	}
	
	public static void send(String channel, MqttMessage msg) {
		
		try {
			sender.publish(channel, msg);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Mediator tested = new Mediator("tcp://localhost:1883", "monitorChannel");
		tested.run();
		
		ConcernBaseEvent<String> genericEvent = new ConcernBaseEvent<String>(
				System.currentTimeMillis(),
				"TesterProbe", "monitoring", "theSessionID", "achecksumField", "EventA", "started", CepType.DROOLS, "extensionForSecurity");

		MqttMessage genericEventInMQTT = new MqttMessage();
		
		JSONObject converted = new JSONObject(genericEvent);
		
		genericEventInMQTT.setPayload(converted.toString().getBytes());
				
		Mediator.send("monitorChannel", genericEventInMQTT);
	}
}
