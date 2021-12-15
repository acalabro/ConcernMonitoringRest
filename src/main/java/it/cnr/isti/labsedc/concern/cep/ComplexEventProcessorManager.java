package it.cnr.isti.labsedc.concern.cep;

import javax.jms.Message;

public abstract class ComplexEventProcessorManager extends Thread{

	public abstract void onMessage(Message message);
	public abstract boolean cepHasCompletedStartup();
}
