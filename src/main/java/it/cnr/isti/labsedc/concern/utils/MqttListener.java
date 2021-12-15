package it.cnr.isti.labsedc.concern.utils;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttListener {

	public static void main(String[] args) {
	
	//MOSQUITOTEST
	try {
		MqttClient client2 = new MqttClient("tcp://0.0.0.0:1883", MqttClient.generateClientId());
		client2.setCallback( new ConcernMQTTCallBack() );
		client2.connect();
		client2.subscribe("iot_data"); 
		System.out.println("listening");
	} catch (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
