package it.cnr.isti.labsedc.concern.cep;

import java.util.Collection;

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
import org.kie.api.definition.KiePackage;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.event.ConcernCANbusEvent;
import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;
import it.cnr.isti.labsedc.concern.register.ChannelsManagementRegistry;

public class EsperComplexEventProcessorManager extends ComplexEventProcessorManager implements MessageListener, MessageAuthorizationPolicy {

    private static Logger logger = LogManager.getLogger(EsperComplexEventProcessorManager.class);
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

	public EsperComplexEventProcessorManager(String instanceName, String staticRuleToLoadAtStartup, String connectionUsername, String connectionPassword, CepType type) {
		super();
		try{
			kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		}catch(Exception e) {
			System.out.println(e.getCause() + "\n"+
			e.getMessage());
		}
		logger = LogManager.getLogger(EsperComplexEventProcessorManager.class);
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
		logger.info("...CEP named " + this.getInstanceName() + " created Session and fires rules " + staticRuleToLoadAtStartup + " with errors: " + kbuilder.getKnowledgePackages());
		started  = true;
		ksession.setGlobal("EVENTS EntryPoint", eventStream);
		eventStream = ksession.getEntryPoint("DEFAULT");
		ConcernApp.componentStarted.put(this.getClass().getSimpleName() + instanceName, true);
		ksession.fireUntilHalt();
	}

	private void communicationSetup() throws JMSException {
		receiverConnection = ChannelsManagementRegistry.GetNewTopicConnection(username, password);
		receiverSession = ChannelsManagementRegistry.GetNewSession(receiverConnection);
		topic = ChannelsManagementRegistry.RegisterNewCepTopic(this.cep.name()+"-"+instanceName, receiverSession, this.cep.name()+"-"+instanceName, ChannelProperties.GENERICREQUESTS, cep);
		logger.info("...CEP named " + this.getInstanceName() + " creates a listening channel called: " + topic.getTopicName());
		MessageConsumer complexEventProcessorReceiver = receiverSession.createConsumer(topic);
		complexEventProcessorReceiver.setMessageListener(this);
		receiverConnection.start();
	}

	@Override
	public void onMessage(Message message) {

		if (message instanceof ObjectMessage) {
			try {
					ObjectMessage msg = (ObjectMessage) message;
					if (msg.getObject() instanceof ConcernCANbusEvent<?>) {
						ConcernCANbusEvent<?> receivedEvent = (ConcernCANbusEvent<?>) msg.getObject();
						insertEvent(receivedEvent);					
					}
					if (msg.getObject() instanceof ConcernEvaluationRequestEvent<?>) {		
						ConcernEvaluationRequestEvent<?> receivedEvent = (ConcernEvaluationRequestEvent<?>) msg.getObject();
						loadRule(receivedEvent);
					} 
				}catch(ClassCastException | JMSException asd) {
					logger.error("error on casting or getting ObjectMessage to GlimpseEvaluationRequestEvent");
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

	private void loadRule(ConcernEvaluationRequestEvent<?> receivedEvent) {
//			
//		String xmlMessagePayload = receivedEvent.getEvaluationRule();
//		String sender = receivedEvent.getSenderID();
//		ComplexEventRuleActionListDocument ruleDoc;
//		
//		ComplexEventRuleActionType rules = ruleDoc.getComplexEventRuleActionList();
//
//		/*
//		// the topic where the listener will give analysis results
//		answerTopic = "answerTopic" + "#" + this.getName() + "#" + System.nanoTime();
//
//		DebugMessages.print(System.currentTimeMillis(), this.getClass().getSimpleName(), "Create answerTopic");
//		connectionTopic = publishSession.createTopic(answerTopic);
//		// tPub = publishSession.createPublisher(connectionTopic);
//		DebugMessages.ok();
//
//		DebugMessages.print(System.currentTimeMillis(), this.getClass().getSimpleName(),
//				"Setting up ComplexEventProcessor with new rule.");
//				*/
//		
//		try {
//			Object[] loadedKnowledgePackage = rulesManagerOne.loadRules(rules);
//
//			// inserisco la coppia chiave valore dove la chiave è il KnowledgePackage
//			// caricato, generato da DroolsRulesManager con la loadRules
//			// e il valore è l'enabler che l'ha inviata
//			// (il KnowledgePackage array dovrebbe avere sempre dimensione 1
//			// essendo creato ad ogni loadrules)
//			for (int i = 0; i < loadedKnowledgePackage.length; i++) {
//				KnowledgePackageImp singleKnowlPack = (KnowledgePackageImp) loadedKnowledgePackage[i];
//				Rule[] singleRuleContainer = new Rule[singleKnowlPack.getRules().size()];
//				singleRuleContainer = singleKnowlPack.getRules().toArray(singleRuleContainer);
//
//				for (int j = 0; j < singleRuleContainer.length; j++) {
//					requestMap.put(singleRuleContainer[j].getName(), new ConsumerProfile(sender, answerTopic));
//				}
//			}
//		
//			sendMessage(createMessage("AnswerTopic == " + answerTopic, sender,0));
//		} catch (IncorrectRuleFormatException e) {
//			sendMessage(createMessage("PROVIDED RULE CONTAINS ERRORS", sender,0));
//		}
//				
//	} catch (NullPointerException asd) {
//		try {
//			sendMessage(createMessage("PROVIDED RULE IS NULL, PLEASE PROVIDE A VALID RULE",
//					msg.getStringProperty("SENDER"),0));
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//	} catch (XmlException e) {
//		try {
//			sendMessage(createMessage("PROVIDED XML CONTAINS ERRORS", msg.getStringProperty("SENDER"),0));
//		} catch (JMSException e1) {
//			e1.printStackTrace();
//		}
//		
//		
//		
		
		
		
		logger.info("...CEP named " + this.getInstanceName() + " receives rules "  + receivedEvent.getData() + " and load it into the knowledgeBase");
	}

	private void insertEvent(ConcernCANbusEvent<?> receivedEvent) {
		if (eventStream != null && receivedEvent != null) {
			eventStream.insert(receivedEvent);
	
			logger.info("...CEP named " + this.getInstanceName() + " insert event "  + receivedEvent.getData() +" in the stream");
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
}
