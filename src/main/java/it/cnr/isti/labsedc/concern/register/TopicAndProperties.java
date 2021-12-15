package it.cnr.isti.labsedc.concern.register;

import it.cnr.isti.labsedc.concern.cep.CepType;
import it.cnr.isti.labsedc.concern.eventListener.ChannelProperties;

public class TopicAndProperties {

	private String localCepIdentifier;
	private ChannelProperties localChannelProp;
	private CepType localCepType;
	private String localTopicAddress;

	public TopicAndProperties(String cepIdentifier, ChannelProperties channelProperty, CepType cepType, String topicAddress) {
		this.localChannelProp = channelProperty;
		this.localCepIdentifier = cepIdentifier;
		this.localCepType = cepType;
		this.localTopicAddress = topicAddress;
	}

	public String getTopicAddress() {
		return this.localTopicAddress;
	}
	
	public void setTopicAddress(String topicAddress) {
		this.localTopicAddress = topicAddress;
	}
	
	public CepType getLocalCepType() {
		return localCepType;
	}
	public void setLocalCepType(CepType localCepType) {
		this.localCepType = localCepType;
	}
	public String getLocalCepIdentifier() {
		return localCepIdentifier;
	}
	public void setLocalQ(String localCepIdentifier) {
		this.localCepIdentifier = localCepIdentifier;
	}
	public ChannelProperties getServiceChannelProperties() {
		return localChannelProp;
	}
	public void setServiceChannelProperties(ChannelProperties localChannelProp) {
		this.localChannelProp = localChannelProp;
	}
}
