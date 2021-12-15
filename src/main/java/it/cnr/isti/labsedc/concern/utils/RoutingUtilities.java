package it.cnr.isti.labsedc.concern.utils;

import java.util.Random;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;
import it.cnr.isti.labsedc.concern.event.ConcernEvaluationRequestEvent;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;
import it.cnr.isti.labsedc.concern.register.ChannelsManagementRegistry;
import it.cnr.isti.labsedc.concern.register.TopicAndProperties;

public class RoutingUtilities {

	private static CepType cepType;
	private static ChannelProperties requestedProperties;

	public static TopicAndProperties BestCepSelectionForRules(ConcernEvaluationRequestEvent<?> message) {

		cepType = message.getCepType();
		requestedProperties = message.getPropertyRequested();
		TopicAndProperties localQaP;

		for (int i = 0; i<ChannelsManagementRegistry.ActiveCep.size();i++) {
			localQaP = (TopicAndProperties) ChannelsManagementRegistry.ActiveCep.keySet().toArray()[i];
			if (cepType ==  localQaP.getLocalCepType() && requestedProperties ==  localQaP.getServiceChannelProperties()) {
				return localQaP;
					}
			}
		return null;
		}
	
	public static TopicAndProperties BestCepSelectionForEvents(ConcernAbstractEvent<?> message) {

		cepType = message.getCepType();
		TopicAndProperties localQaP;
		int maxSize = ChannelsManagementRegistry.ActiveCep.size();
		for (int i = 0; i<maxSize;i++) {
			Random rand = new Random();
			int theRand = rand.ints(0, maxSize)
		      .findFirst()
		      .getAsInt();
			localQaP = (TopicAndProperties) ChannelsManagementRegistry.ActiveCep.keySet().toArray()[theRand];
			if (cepType ==  localQaP.getLocalCepType()) {
				return localQaP;
					}
			}
		return null;
		}
	
}
