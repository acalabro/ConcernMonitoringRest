package it.cnr.isti.labsedc.concern.notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.ConcernApp;

public class NotificationManager extends Thread {

	private static Logger logger;
	
	public NotificationManager() {
		ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
		logger = LogManager.getLogger(NotificationManager.class);

	}
	
	public static void NotifyToConsumer(String consumerName, String violationMessage) {
		logger.info("\nReceived violation event from: " + consumerName + "\nRaised violation: " + violationMessage + "\n\n");
//		try {
//			logger.info("Creating response topic");
//			topic = session.createTopic(ChannelsManagementRegistry.getConsumerChannel(consumerName));
//
//        MessageProducer producer = session.createProducer(topic);
//		TextMessage msg = session.createTextMessage(violationMessage);
//		producer.send(msg);
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static void NotifyToGroundStation(String port, String notificationMessage) {
		LoRaOnSerialWriter writer = new LoRaOnSerialWriter();
		writer.connect(port);
		writer.write(notificationMessage);
		writer.closePort();
	}

	public static void NotifyToTheOrchestrator(String whatToNotify) {
//		HttpClient httpclient = HttpClients.createDefault();
//		HttpPost httppost = new HttpPost("http://www.a-domain.com/foo/");
//
//		// Request parameters and other properties.
//		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//		params.add(new BasicNameValuePair("param-1", "12345"));
//		params.add(new BasicNameValuePair("param-2", "Hello!"));
//		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//
//		//Execute and get the response.
//		HttpResponse response = httpclient.execute(httppost);
//		HttpEntity entity = response.getEntity();
//
//		if (entity != null) {
//		    try (InputStream instream = entity.getContent()) {
//		        // do something useful
//		    }
//		}

	/*	try {
		
			var values = new HashMap<String, String>() {
				private static final long serialVersionUID = 1298759667309051214L;
				{
		            put("name", "John Doe");
		            put ("occupation", "gardener");
		        }
			};

			ObjectMapper objectMapper = new ObjectMapper();
	        String requestBody = objectMapper
	                .writeValueAsString(values);
	        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/post"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response;

			response = client.send(request,
			        HttpResponse.BodyHandlers.ofString());

	    System.out.println(response.body());
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		*/
	}
	
	
	@Override
	public void run() {

	//TODO:
		
	}
}
