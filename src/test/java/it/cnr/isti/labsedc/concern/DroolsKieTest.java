package it.cnr.isti.labsedc.concern;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieServices;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;

public class DroolsKieTest extends Thread {

	private static KnowledgeBuilder kbuilder;
	private static InternalKnowledgeBase kbase;
	private static KiePackage pack;
	private static KieSession ksess;

	public DroolsKieTest() {

		sysStartup();
}

	   public static void main(String[] args) {}
	   

	static void sysStartup() {
		kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
	    kbuilder.add(ResourceFactory.newFileResource(System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl"), ResourceType.DRL);
	    if (kbuilder.hasErrors()) {	
	        throw new RuntimeException("Errors: " + kbuilder.getErrors());
	    }
	    kbase = KnowledgeBaseFactory.newKnowledgeBase();

	    kbase.addPackages(kbuilder.getKnowledgePackages());
	    	    
	    KieSessionConfiguration config = KieServices.Factory.get().newKieSessionConfiguration();
	    config.setOption( ClockTypeOption.get("realtime") );
	    ksess = kbase.newKieSession( config, null );
	   
	    printcose();
	    
	}

	@Override
	public void run() {
		System.out.println("start");
		ksess.fireUntilHalt();
	}

	static void loadRule() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println("metto altra regola");
	    kbuilder.add(ResourceFactory.newFileResource(System.getProperty("user.dir")+ "/src/main/resources/testSequence.drl"), ResourceType.DRL);
	    kbase.addPackages(kbuilder.getKnowledgePackages());
	    printcose();
	}
	static void printcose() {
		Object[] packages = kbase.getKiePackages().toArray();
		for (int m = 0; m< packages.length; m++) {
		System.out.println("Quante regole dentro package: " + ((KiePackage)packages[m]).getName() + " " + ((KiePackage)packages[m]).getRules().size());
		}
	    for (int i = 0; i< packages.length; i++) {
	    	if (((KiePackage)packages[i]).getFactTypes().size() > 0) {
	    		for (int j = 0; j< ((KiePackage)packages[i]).getFactTypes().size(); j++) {
		    	KiePackage asd = ((KiePackage)packages[i]);
		    	Object[] facts = asd.getFactTypes().toArray();
		    	System.out.println("--> Facts: "+ ((FactType)facts[j]).getName() + " SimpleName: " + ((FactType)facts[j]).getSimpleName());
	    		}
	    	}
	    	
	    	if (((KiePackage)packages[i]).getRules().size() > 0) {
	    		for (int j = 0; j< ((KiePackage)packages[i]).getRules().size(); j++) {
		    	KiePackage asd = ((KiePackage)packages[i]);
		    	Object[] rules = asd.getRules().toArray();
		    	System.out.println("--> Rules: " + ((Rule)rules[j]).getName() + " PackageName: " + ((Rule)rules[j]).getPackageName());
	    		}
	    	}
	    
	    }
	    System.out.println("-------SESSION-----");
	    kbase.getKiePackages().forEach((e -> {System.out.println("Package Name " + e.getName() + " Rules " + e.getRules().size());}));
	}
	static void insertEvent(ConcernAbstractEvent<?> evy) {
		System.out.println(ksess.getFactCount());
		ksess.insert(evy);
		System.out.println(ksess.getFactCount());;
		
	}
}
