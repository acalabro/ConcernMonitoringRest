package it.cnr.isti.labsedc.concern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.fazecast.jSerialComm.SerialPort;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;

public class GPSProbe {

	static String deviceGPS = "ttyACM0";
	static SerialPort comPort;
	static OutputStream out;
	static String brokerUrl = "tcp://0.0.0.0:61616";

	public static String lastGPSpos = null;
	
	public static void main(String[] args) throws InterruptedException {
		loopThreadGPS();

	}

	private static void loopThreadGPS() {
		try {	
			Process p = Runtime.getRuntime().exec("cat /dev/" + deviceGPS);
		new Thread(new Runnable() {
		    public void run() {
		    	System.out.println("GPS Probe started");       		
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line = null;
		        String[] results;
		        try {
		    			
		            while ((line = input.readLine()) != null)
		            	
		            	if (line != null && line.startsWith("$GPGLL")) //  || line.contains("signal:"))
	                	{
		            		results = line.split(",");
		            		if (results[6].compareTo("A") == 0) { //gps signal is valid
		            			testProbe(brokerUrl, "DROOLS-InstanceOne", "vera", "griselda", "Robot-TWO", results[1]+","+results[3]);
		            		}
	                	}
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}).start();

			p.waitFor();
		} catch (InterruptedException | IOException e1) {
			e1.printStackTrace();
		}
	}	
	
	public static void testProbe(String brokerUrl, String topicName, String username, String password, String eventData, String eventName) {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
			Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);     
			ObjectMessage msg = session.createObjectMessage();
			
			ConcernBaseEvent<String> event = new ConcernBaseEvent<String>(
					System.currentTimeMillis(), 
					new Exception().getStackTrace()[1].getClassName(),
					"AuditingSystem-Monitoring", "sessionA", 
					"checksum",
					eventName, eventData, CepType.DROOLS,"extension");
 				msg.setObject(event);
				producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}