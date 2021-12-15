package it.cnr.isti.labsedc.concern.requestListener;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.ConcernApp;

public class ServiceListenerManager extends Thread {

	private Vector<String> loadChannels;
	private String username;
	private String password;
	private static boolean killAll = true;
    private static final Logger logger = LogManager.getLogger(ServiceListenerManager.class);

	public ServiceListenerManager(Vector<String> loadChannels, String connectionUsername, String connectionPassword) {
		this.loadChannels = loadChannels;
		this.username = connectionUsername;
		this.password = connectionPassword;
	}

	public void run() {

			ServiceListenerManager.killAll = false;
			ExecutorService executor = Executors.newFixedThreadPool(loadChannels.size());
			logger.info("Creating executors");
			for (int i = 0; i< loadChannels.size(); i++) {
				Runnable worker = new ServiceListenerTask(loadChannels.get(i), username, password);
				executor.execute(worker);
			}
			ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
			while(!ServiceListenerManager.killAll) {
			}
			logger.info("KILALLLLLLLL");
			executor.shutdown();
			executor.notifyAll();
			List<Runnable> stillActive = executor.shutdownNow();
			for (int i = 0; i< stillActive.size();i++) {
				System.out.println(stillActive.get(i));
			}
	}

	public static void killAllServiceListeners() {
		ServiceListenerManager.killAll = true;
	}

}
