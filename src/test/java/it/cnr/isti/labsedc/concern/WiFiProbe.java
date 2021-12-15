package it.cnr.isti.labsedc.concern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class WiFiProbe {
	
	static String payload = "";
	
	public static void main (String[] args) {
		
		MqttClient client;
		
	try {
		client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		client.connect();
		MqttMessage message = new MqttMessage();
		
		//iw dev wlp0s20f3 station dump
		
		final Process p = Runtime.getRuntime().exec("tcpdump -i wlx984827c6f74a -e type mgt");

		new Thread(new Runnable() {
		    public void run() {
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line = null;

		        try {
		            while ((line = input.readLine()) != null)
		            	if (line.startsWith("Station")) //  || line.contains("signal:"))
	                	{
		            		payload+= line.substring(8, 26);
	                	} else 
	                		if (line.contains("signal:"))
	                		{
	                			payload+=line.substring(8,15).trim();
	                		}
		            message.setPayload(payload.getBytes());
		    		client.publish("serotoninData", message);
		    		payload = "";
		        } catch (IOException | MqttException e) {
		            e.printStackTrace();
		        }
		    }
		}).start();

		p.waitFor();

	} catch (MqttException | IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
