package it.cnr.isti.labsedc.concern;

import java.net.UnknownHostException;
import java.util.HashMap;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.ConsumerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import it.cnr.isti.labsedc.concern.broker.ActiveMQBrokerManager;
import it.cnr.isti.labsedc.concern.broker.BrokerManager;
import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.cep.ComplexEventProcessorManager;
import it.cnr.isti.labsedc.concern.cep.DroolsComplexEventProcessorManager;
import it.cnr.isti.labsedc.concern.consumer.Consumer;
import it.cnr.isti.labsedc.concern.notification.NotificationManager;
import it.cnr.isti.labsedc.concern.probe.DTProbe;
import it.cnr.isti.labsedc.concern.register.ChannelsManagementRegistry;
import it.cnr.isti.labsedc.concern.requestListener.ServiceListenerManager;
import it.cnr.isti.labsedc.concern.storage.MySQLStorageController;
import it.cnr.isti.labsedc.concern.utils.Sub;

public class ConcernApp extends Thread
{
	private static BrokerManager broker;
	private static ComplexEventProcessorManager cepMan;
	private static NotificationManager notificationManager;
	private static ChannelsManagementRegistry channelRegistry;
	private static MySQLStorageController storageManager;
	
	private static String brokerUrlJMS;
	private static Long maxMemoryUsage;
	private static Long maxCacheUsage;
	public static ActiveMQConnectionFactory factory;
    public static Logger logger = null;
	private static final boolean SHUTDOWN = false;
	public static HashMap<String, Boolean> componentStarted = new HashMap<String, Boolean>();
	private static String username;
	private static String password;
	private static boolean LOCALBROKER = true;
	private static boolean runningInJMS = true;
	private static String mqttBrokerUrl;
	private static MqttClient listenerClient;

	private static Thread INSTANCE;
        
    public static Thread getInstance() throws InterruptedException {
        if(INSTANCE == null) {
        	INSTANCE = new ConcernApp();
            INSTANCE.run();
        }
        return INSTANCE;
    }
    
    public static boolean isRunning() {
    	if (INSTANCE == null)
    		return false;
    	return true;
    }
    	
    @SuppressWarnings("deprecation")
	public static void killInstance() {
    	ConcernApp.broker.stopActiveMQBroker();
    	INSTANCE.interrupt();
    	INSTANCE.stop();
    	INSTANCE = null;
    }
    
    public static void main( String[] args ) throws InterruptedException
    {
    	logger = LogManager.getLogger(ConcernApp.class);
    	username = "vera";
    	password = "griselda";
    	
    	//brokerUrl = "tcp://sedc-nethd.isti.cnr.it:49195";
    	if(runningInJMS) {
    	brokerUrlJMS = "tcp://0.0.0.0:61616";
    	maxMemoryUsage = 128000l;
    	maxCacheUsage = 128000l;
    	factory = new ActiveMQConnectionFactory(brokerUrlJMS);
    	logger.info(Sub.newSessionLogger());
    	logger.info("Starting components");
    	StartComponents(factory, brokerUrlJMS, maxMemoryUsage, maxCacheUsage);
    	}
    	else{
    		mqttBrokerUrl="tcp://localhost:1183";
    		logger.debug("Starting components");
    		StartComponents(listenerClient,mqttBrokerUrl, "serotoninData");
    	}
    }

    public static void DemoStart() {

    	System.out.println("------------------------------------------------------");
    	System.out.println("---------Demo session will starts in 3 seconds--------");
    	System.out.println("------------------------------------------------------");
    	long timer = System.currentTimeMillis();
    	while (System.currentTimeMillis() < (timer+3000)) {
    		
    	}
    	System.out.println("------------------------------------------------------");
    	System.out.println("-------Starting Auditing Framework Demo session-------");
    	System.out.println("------------------------------------------------------");
    	Thread ruleSender = new Consumer();
		ruleSender.start();
    	timer = System.currentTimeMillis();
    	while (System.currentTimeMillis() < (timer+3000)) {
    		
    	}    	
    	System.out.println("------------------------------------------------------");
    	System.out.println("-------------Rules and metarules injected-------------");
    	System.out.println("------------------------------------------------------");
    	System.out.println("-------------------Starting DTProbe-------------------");
    	try {
			DTProbe.main(null);
		} catch (InterruptedException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("------------------------------------------------------");
    	System.out.println("-------------------DT Probe started-------------------");
    	System.out.println("------------------------------------------------------");
    	
    }
    
    public static void StartComponents(MqttClient listenerClient, String mqttBrokerUrl, String topic) {
		try {
			channelRegistry = new ChannelsManagementRegistry();

	    	logger.debug("Channels Management Registry created");
	    	String CEPInstanceName = "InstanceOne";
	    	listenerClient = new MqttClient("tcp://0.0.0.0:1883",MqttClient.generateClientId());
	    	ChannelsManagementRegistry.setMqttClient(listenerClient);
	    	ChannelsManagementRegistry.setMqttChannel(topic);
	    	
	    	storageManager = new MySQLStorageController();
	    	storageManager.connectToDB();
	    	  	
	    	notificationManager = new NotificationManager();
	    	notificationManager.start();
	    	
	    	//STARTING CEP ONE
	    	cepMan = new DroolsComplexEventProcessorManager(
	    			CEPInstanceName,
	    			System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl",
	    			username, 
	    			password, CepType.DROOLS,
	    			runningInJMS);
	    	cepMan.start();

	    	while (!cepMan.cepHasCompletedStartup()) {
	    		System.out.println("wait for First CEP start");
	    		Thread.sleep(100);
	    	}
	    	
	    	if(SHUTDOWN) {
		    	ServiceListenerManager.killAllServiceListeners();
		    	
		    	System.exit(0);
	    	}
		} catch (MqttException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
	public static void StartComponents(ActiveMQConnectionFactory factory, String brokerUrl, long maxMemoryUsage, long maxCacheUsage) throws InterruptedException {

		//storage = new InfluxDBStorageController();
		if (LOCALBROKER) {
			broker = new ActiveMQBrokerManager(brokerUrl, maxMemoryUsage, maxCacheUsage, username, password);
			logger.debug(ConcernApp.class.getSimpleName() + " is launching the broker.");
			broker.run();
			logger.debug(ConcernApp.class.getSimpleName() + " broker launched.");
		} 
		factory = new ActiveMQConnectionFactory(username, password, brokerUrl);
		
		channelRegistry = new ChannelsManagementRegistry();

    	logger.debug("Channels Management Registry created");
    	System.out.println("PATH: " + System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl");
    	channelRegistry.setConnectionFactory(factory);

    	storageManager = new MySQLStorageController();
    	storageManager.connectToDB();
    	  	
    	notificationManager = new NotificationManager();
    	notificationManager.start();
    	
    	//STARTING CEP ONE
    	cepMan = new DroolsComplexEventProcessorManager(
    			"InstanceOne",
    			System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl",
    			username, 
    			password, CepType.DROOLS,
    			runningInJMS);
    	cepMan.start();

    	while (!cepMan.cepHasCompletedStartup()) {
    		System.out.println("wait for First CEP start");
    		Thread.sleep(100);
    	}
    	
    	if(SHUTDOWN) {
	    	ServiceListenerManager.killAllServiceListeners();
	    	ActiveMQBrokerManager.StopActiveMQBroker();
	    	System.exit(0);
    	}
	}

	@Override
	public void run() {
		try {
			ConcernApp.main(null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String getLoggerData() {
		return Sub.readFile(System.getProperty("user.dir")+ "/logs/app-debug.log");
	}
}
