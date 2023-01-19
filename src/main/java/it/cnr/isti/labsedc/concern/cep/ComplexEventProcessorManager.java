package it.cnr.isti.labsedc.concern.cep;

import java.util.ArrayList;

import javax.jms.Message;

public abstract class ComplexEventProcessorManager extends Thread{

	public abstract void onMessage(Message message);
	public abstract boolean cepHasCompletedStartup();
	public abstract int getAmountOfLoadedRules();
	public abstract String getLastRuleLoadedName();
	public abstract ArrayList<String> getRulesList();
}
