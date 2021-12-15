package it.cnr.isti.labsedc.concern.broker;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.SslBrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.usage.SystemUsage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.ConcernApp;

public class ActiveMQBrokerManager implements BrokerManager, Runnable {

	private static BrokerService broker;
	private static TransportConnector connector;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection conn;
	private static long ACTIVEMQ_MEMORY_USAGE = 0;
	private static long ACTIVEMQ_TEMP_USAGE = 0;
	private static String ACTIVEMQ_HOST;
    private static final Logger logger = LogManager.getLogger(ActiveMQBrokerManager.class);
	private static String ACTIVEMQ_LOGINNAME;
	private static String ACTIVEMQ_PASSWORD;

	public ActiveMQBrokerManager(String instanceExecutionHost, Long maxMemoryUsage, Long maxCacheUsage, String loginName, String password) {
		ACTIVEMQ_MEMORY_USAGE = maxMemoryUsage;
		ACTIVEMQ_TEMP_USAGE = maxCacheUsage;
		ACTIVEMQ_HOST = instanceExecutionHost;
		ACTIVEMQ_LOGINNAME = loginName;
		ACTIVEMQ_PASSWORD = password;
	}

	@Override
	public void run() {
		logger.info("Start SSL Broker");
		broker = new SslBrokerService();
		broker.setPersistent(false);

		try {
			logger.debug("Creating TrasportConnector");
			connector = new TransportConnector();
			connector.setUri(new URI(ACTIVEMQ_HOST));
			broker.addConnector(connector);
			broker.setUseJmx(false);

			SystemUsage systemUsage= broker.getSystemUsage();
			systemUsage.getMemoryUsage().setLimit(ACTIVEMQ_MEMORY_USAGE);
			systemUsage.getTempUsage().setLimit(ACTIVEMQ_TEMP_USAGE);

			broker.start();
			logger.debug("Waiting for broker start");
			broker.waitUntilStarted();
			logger.debug("broker started");

			logger.debug("Enabling SSL ConnectionFactory");
			conn = singletonActiveMQ();
			logger.debug("Connection sucessfully created");
			ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
	    	logger.debug("Component "+ this.getClass().getSimpleName() + " loaded in registry.");
		} catch (Exception e) {
			logger.error(e.getCause());
		}
	}

	public static Connection singletonActiveMQ() throws JMSException {
		if (connectionFactory == null) {
		connectionFactory = new ActiveMQSslConnectionFactory(ACTIVEMQ_HOST);
		connectionFactory.setUserName(ACTIVEMQ_LOGINNAME);
		connectionFactory.setPassword(ACTIVEMQ_PASSWORD);
		conn = connectionFactory.createConnection();
		}
		return conn;
	}

	public static void StopActiveMQBroker() {
		try {
			broker.stop();
			logger.debug("Waiting for broker stop");
			broker.waitUntilStopped();
			logger.debug("broker stopped");
			connector.stop();
			logger.debug("Connector Stopped");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopActiveMQBroker() {
		try {
			broker.stop();
			logger.debug("Waiting for broker stop");
			broker.waitUntilStopped();
			logger.debug("broker stopped");
			connector.stop();
			logger.debug("Connector Stopped");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
