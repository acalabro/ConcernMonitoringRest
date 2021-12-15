package it.cnr.isti.labsedc.concern.eventListener;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.storage.StorageController;

public class EventListenerManager extends Thread {

	private Vector<String> loadChannels;
	private String username;
	private String password;
	private StorageController storageManager;
	private static boolean killAll = true;
    private static final Logger logger = LogManager.getLogger(EventListenerManager.class);

	public EventListenerManager(Vector<String> loadChannels, String connectionUsername, String connectionPassword, StorageController storageManager) {
		this.loadChannels = loadChannels;
		this.username = connectionUsername;
		this.password = connectionPassword;
		this.storageManager = storageManager;
	}

	public void run() {
			EventListenerManager.killAll = false;
			ExecutorService executor = Executors.newFixedThreadPool(loadChannels.size());
			logger.info("Creating event listerner");
			for (int i = 0; i< loadChannels.size(); i++) {
				Runnable worker = new EventListenerTask(loadChannels.get(i), username, password, storageManager);
				executor.execute(worker);
			}
			ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
			while(!EventListenerManager.killAll) {
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
		EventListenerManager.killAll = true;
	}

}
