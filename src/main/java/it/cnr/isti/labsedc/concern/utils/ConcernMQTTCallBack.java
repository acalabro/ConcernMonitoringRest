package it.cnr.isti.labsedc.concern.utils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ConcernMQTTCallBack implements MqttCallback {

	public void connectionLost(Throwable throwable) {
	    System.out.println("Connection to MQTT broker lost!");
	  }

	public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
	    System.out.println("Message received:\n\t"+ new String(mqttMessage.getPayload()) );
	  }
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}	
}
