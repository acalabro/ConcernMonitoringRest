package it.cnr.isti.labsedc.concern.cep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;

import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.security.MessageAuthorizationPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;
import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;
import it.cnr.isti.labsedc.concern.event.ConcernCmdVelEvent;
import it.cnr.isti.labsedc.concern.event.ConcernDTForecast;
import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.event.ConcernICTGatewayEvent;
import it.cnr.isti.labsedc.concern.event.ConcernNetworkEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;
import it.cnr.isti.labsedc.concern.register.ChannelsManagementRegistry;
import it.cnr.isti.labsedc.concern.utils.ConcernMQTTCallBack;

public class DroolsComplexEventProcessorManager extends ComplexEventProcessorManager implements MessageListener, MessageAuthorizationPolicy {

    private static Logger logger = LogManager.getLogger(DroolsComplexEventProcessorManager.class);
	private TopicConnection receiverConnection;
	private Topic topic;
	private Session receiverSession;
	private CepType cep;
	private String instanceName;
	private String staticRuleToLoadAtStartup;
	private boolean started = false;
	private String username;
	private String password;

	private static KnowledgeBuilder kbuilder;
    private static Collection<KiePackage> pkgs;
    private static InternalKnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    private static KieSession ksession;
	private EntryPoint eventStream;
	private boolean isUsingJMS = true;
	public static ArrayList<String> rulesNames;
	public static int totalRulesLoaded = 0;
	public static String lastRuleLoadedName;

	public DroolsComplexEventProcessorManager(String instanceName, String staticRuleToLoadAtStartup, 
												String connectionUsername, String connectionPassword, 
												CepType type, boolean runningInJMS) {
		super();
		isUsingJMS = runningInJMS;
		try{
			kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		}catch(Exception e) {
			System.out.println(e.getCause() + "\n"+
			e.getMessage());
		}
		logger = LogManager.getLogger(DroolsComplexEventProcessorManager.class);
		logger.info("CEP creation ");
		this.cep = type;
		this.instanceName = instanceName;
		this.staticRuleToLoadAtStartup = staticRuleToLoadAtStartup;
		this.username = connectionUsername;
		this.password = connectionPassword;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	@Override
	public void run() {
		try {
			communicationSetup();
			droolsEngineSetup();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void droolsEngineSetup() {
		Resource drlToLoad = ResourceFactory.newFileResource(staticRuleToLoadAtStartup);
        kbuilder.add(drlToLoad, ResourceType.DRL);

        if(kbuilder.hasErrors()) {
            System.out.println(kbuilder.getErrors().toString());
            throw new RuntimeException("unable to compile dlr");
        }

        pkgs = kbuilder.getKnowledgePackages();
        kbase.addPackages(pkgs);
        ksession = kbase.newKieSession();
		logger.info("...CEP named " + this.getInstanceName() + " created Session and fires rules " + staticRuleToLoadAtStartup + " with knowledgePackages: " + kbuilder.getKnowledgePackages());
		started  = true;
		ksession.setGlobal("EVENTS EntryPoint", eventStream);
		eventStream = ksession.getEntryPoint("DEFAULT");
		ConcernApp.componentStarted.put(this.getClass().getSimpleName() + instanceName, true);
		ksession.fireUntilHalt();
	}

	private void communicationSetup() throws JMSException {
		if (isUsingJMS) {
			receiverConnection = ChannelsManagementRegistry.GetNewTopicConnection(username, password);
			receiverSession = ChannelsManagementRegistry.GetNewSession(receiverConnection);
			topic = ChannelsManagementRegistry.RegisterNewCepTopic(this.cep.name()+"-"+instanceName, receiverSession, this.cep.name()+"-"+instanceName, ChannelProperties.GENERICREQUESTS, cep);
			logger.info("...CEP named " + this.getInstanceName() + " creates a listening channel called: " + topic.getTopicName());
			MessageConsumer complexEventProcessorReceiver = receiverSession.createConsumer(topic);
			complexEventProcessorReceiver.setMessageListener(this);
			receiverConnection.start();
		} else {
			MqttClient listener = ChannelsManagementRegistry.getMqttClient();
			listener.setCallback( new ConcernMQTTCallBack() );
			try {
				listener.connect();
				listener.subscribe(ChannelsManagementRegistry.getMqttChannel()); 
				logger.info("...CEP named " + this.getInstanceName() + " is listening on " + ChannelsManagementRegistry.getMqttChannel());
			} catch (MqttSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onMessage(Message message) {

		if (message instanceof ObjectMessage) {
			try {
					ObjectMessage msg = (ObjectMessage) message;
					if (msg.getObject() instanceof ConcernBaseEvent<?>) {
						ConcernBaseEvent<?> receivedEvent = (ConcernBaseEvent<?>) msg.getObject();
						insertEvent(receivedEvent);					
					} else {
						if (msg.getObject() instanceof ConcernDTForecast<?>) {
							ConcernDTForecast<?> receivedEvent = (ConcernDTForecast<?>) msg.getObject();
							insertEvent(receivedEvent);					
						} else {
							if (msg.getObject() instanceof ConcernCmdVelEvent<?>) {
								ConcernCmdVelEvent<?> receivedEvent = (ConcernCmdVelEvent<?>) msg.getObject();
								insertEvent(receivedEvent);			
							} else {
								if (msg.getObject() instanceof ConcernNetworkEvent<?>) {
									ConcernNetworkEvent<?> receivedEvent = (ConcernNetworkEvent<?>) msg.getObject();
									insertEvent(receivedEvent);
								} else {
									if (msg.getObject() instanceof ConcernICTGatewayEvent<?>) {
										ConcernICTGatewayEvent<?> receivedEvent = (ConcernICTGatewayEvent<?>) msg.getObject();
										insertEvent(receivedEvent);		
									} else {
//										if(msg.getObject() instanceof ConcernAnemometerEvent<?>) {
//											ConcernAnemometerEvent<?> receivedEvent = (ConcernAnemometerEvent<?>) msg.getObject();
//											insertEvent(receivedEvent);
//										} else {
											if (msg.getObject() instanceof ConcernEvaluationRequestEvent<?>) {		
												ConcernEvaluationRequestEvent<?> receivedEvent = (ConcernEvaluationRequestEvent<?>) msg.getObject();
												if (receivedEvent.getCepType() == CepType.DROOLS) {
													logger.info("...CEP named " + this.getInstanceName() + " receives rules "  + receivedEvent.getData() );
													loadRule(receivedEvent);
												}
											}
										//}
									}
								}
							}
						}
					}
				}	catch(ClassCastException | JMSException asd) {
					logger.debug(asd.getMessage());
					logger.debug("error on casting or getting ObjectMessage");
			}
		}
		if (message instanceof TextMessage) {
			TextMessage msg = (TextMessage) message;
			try {
				logger.info("CEP " + this.instanceName + " receives TextMessage: " + msg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadRule(ConcernEvaluationRequestEvent<?> receivedEvent) {
		Object[] packages = kbase.getKiePackages().toArray();
		for (int m = 0; m< packages.length; m++) {
		}
		Resource drlToLoad = ResourceFactory.newByteArrayResource(receivedEvent.getData().toString().getBytes());
		DroolsComplexEventProcessorManager.lastRuleLoadedName = receivedEvent.getEvaluationRuleName();
        kbuilder.add(drlToLoad, ResourceType.DRL);
        pkgs = kbuilder.getKnowledgePackages();
        kbase.addPackages(pkgs);

        if(kbuilder.hasErrors()) {
            System.out.println(kbuilder.getErrors().toString());
            throw new RuntimeException("unable to compile dlr");
        }
        logger.info("...CEP named " + this.getInstanceName() + " load rules received into the knowledgeBase");
        rulesCounter();
	}
	
	private void rulesCounter() {
		Object[] packages2 = kbase.getKiePackages().toArray();
        DroolsComplexEventProcessorManager.rulesNames = new ArrayList<String>();
        DroolsComplexEventProcessorManager.totalRulesLoaded = 0;
		for (int m = 0; m< packages2.length; m++) {
			int RulesForPackage = ((KiePackage)packages2[m]).getRules().size();
			Iterator<Rule> coll = ((KiePackage)packages2[m]).getRules().iterator();
			while (coll.hasNext()) {
				DroolsComplexEventProcessorManager.rulesNames.add(coll.next().getName());
			}			
			DroolsComplexEventProcessorManager.totalRulesLoaded  = DroolsComplexEventProcessorManager.totalRulesLoaded + RulesForPackage;
		logger.info("How many rules within package: " + ((KiePackage)packages2[m]).getName() + " " + RulesForPackage);
		}		
	}

	@Override
	public boolean deleteRule(String ruleName) {
		Object[] packages = kbase.getKiePackages().toArray();
		for (int m = 0; m< packages.length; m++) {
			Collection<Rule> rls = ((KiePackage)packages[m]).getRules();
			Object[] rulesArray = rls.toArray();
			if (rulesArray.length > 0) {
				for (int j = 0; j<rulesArray.length;j++) {
					Rule gg = (Rule) rulesArray[j];
					if (gg.getName().compareTo(ruleName) == 0)
					{
						kbase.removeRule(
								((KiePackage)packages[m]).getName(), ruleName);
						rulesCounter();
						logger.info("Rule " + ruleName + " removed");
						logger.info("Rule active: " + kbase.getKiePackage(((KiePackage)packages[m]).getName()).getRules().size());
						return true;
					}
				}
			}
		}
		logger.info("Rule " + ruleName + " not found");
		return false;
	}

	private void insertEvent(ConcernAbstractEvent<?> receivedEvent) {
		if (eventStream != null && receivedEvent != null) {
			eventStream.insert(receivedEvent);
			logger.debug("...CEP named " + this.getInstanceName() + " received an event of type:\n"  + receivedEvent.getClass().getCanonicalName() +" in the stream, sent from " + receivedEvent.getSenderID());
			if (receivedEvent instanceof ConcernBaseEvent<?>) {
				ConcernApp.storageManager.saveMessage(receivedEvent);
				logger.debug("with data:" +
						"\nName: "+ receivedEvent.getName() +
						"\nDestination: " + receivedEvent.getDestinationID() +
						"\nData: " + receivedEvent.getData() +
						"\nSenderID: " + receivedEvent.getSenderID() +
						"\nTimestamp: " + receivedEvent.getTimestamp() +
						"\nSessionID: " + receivedEvent.getSessionID() +
						"\nChecksum: " + receivedEvent.getChecksum() +
						"\nCepType: " + receivedEvent.getCepType().toString());
			
			}
//			if (receivedEvent instanceof ConcernAnemometerEvent<?>) {
//				ConcernApp.storageManager.saveWindData((ConcernAnemometerEvent<?>)receivedEvent);
//			}
		}			
	}

	@Override
	public boolean cepHasCompletedStartup() {
		return started;
	}

	@Override
	public boolean isAllowedToConsume(ConnectionContext context, org.apache.activemq.command.Message message) {
		System.out.println("asd");
		return false;
	}
	
	@Override
	public int getAmountOfLoadedRules() {
		// TODO Auto-generated method stub
		return totalRulesLoaded;
	}
	
	@Override
	public String getLastRuleLoadedName() {
		return lastRuleLoadedName;
	}

	@Override
	public ArrayList<String> getRulesList() {
		return DroolsComplexEventProcessorManager.rulesNames;
	}
}