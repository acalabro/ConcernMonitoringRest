package it.cnr.isti.labsedc.concern.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.ConcernApp;
import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;
import it.cnr.isti.labsedc.concern.event.ConcernAnemometerEvent;
import it.cnr.isti.labsedc.concern.event.Event;

public class MySQLStorageController implements StorageController {

    private static final Logger logger = LogManager.getLogger(MySQLStorageController.class);
    
	public String serverAddress = "localhost"; //default is localhost
	public int serverPort = 3306; //default is mysql
	public String username = "concern";
	public String password = "unsecure";
	public String dbName = "concern";
	private Connection con;
    
	public MySQLStorageController(String serverAddress, int serverPort, String username, String password, String collectionName) {

		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.username = username;
		this.password = password;
		this.dbName = collectionName;
		
		logger.info("Setting up storage with specific parameters");
		}
	
	public MySQLStorageController() {
		
		logger.info("Setting up storage with default parameters");
		}

	@Override
	public boolean connectToDB() {
		try{  
			if(con == null || con.isClosed() == true) {
				Class.forName("com.mysql.cj.jdbc.Driver");  
				con=DriverManager.getConnection(  
				"jdbc:mysql://"+serverAddress+":"+serverPort+"/"+dbName,username,password);
				logger.info("Connected successfully to "+dbName+" on "+serverAddress);
				ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
				return true;
			}
			logger.error("Connection failed to "+dbName+" on "+serverAddress);
			return false;
		}
		catch(SQLException | ClassNotFoundException e) {
			logger.warn("Unable to connect to MYSQL instance");
			return false;
		}
	}

	@Override
	public boolean disconnectFromDB() {
		try {
			if (con.isClosed() == false)
			{
				con.close();
				return true;
			}
		} catch (SQLException e) {
		}
		return false;
	}

	@Override
	public boolean saveMessage(Event<?> message) {
		try {
			if (this.con != null && !this.con.isClosed()) {
					 String query = " insert into event (senderID, timestamp, data, dataClassName  )"
						        + " values (?, ?, ?, ?)";
			
				      // create the mysql insert preparedstatement
				      PreparedStatement preparedStmt = this.con.prepareStatement(query);
				      preparedStmt.setString (1, message.getSenderID());
				      preparedStmt.setLong (2, message.getTimestamp());
				      preparedStmt.setObject(3, message.getData());
				      preparedStmt.setString(4, message.getData().getClass().getCanonicalName());
				      // execute the preparedstatement
				      preparedStmt.execute();
				      logger.info("Event: " + message.toString() + " stored.");
				      return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Failure on storing event on db");
			return false;
		}
		return false;
	}

	public boolean saveViolation(String eventTriggeredBy, String violationMessage, String ruleViolated,
			long currentTimeMillis) {
		try {
			if (this.con != null && !this.con.isClosed()) {
					 String query = " insert into violation (violationMessage, probeNameThatTriggersError, ruleViolatedName, violationTimestamp  )"
						        + " values (?, ?, ?, ?)";
			
				      // create the mysql insert preparedstatement
				      PreparedStatement preparedStmt = this.con.prepareStatement(query);
				      preparedStmt.setString (1, violationMessage);
				      preparedStmt.setString (2, eventTriggeredBy);
				      preparedStmt.setString(3, ruleViolated);
				      preparedStmt.setLong(4, currentTimeMillis);
				      // execute the preparedstatement
				      preparedStmt.execute();
				      logger.info("Violation on rule " + ruleViolated + " stored.");
				      return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Failure on storing violation on db");
			return false;
		}
		return false;		
	}

	public boolean saveViolation(String eventTriggeredBy, String violationMessage, String ruleViolated,
			long currentTimeMillis, Map<String, Object> metaData) {

		try {
			if (this.con != null && !this.con.isClosed()) {
					 String query = " insert into violation (violationMessage, probeNameThatTriggersError, ruleViolatedName, violationTimestamp, ruleMetadata  )"
						        + " values (?, ?, ?, ?, ?)";			
				      // create the mysql insert preparedstatement
				      PreparedStatement preparedStmt = this.con.prepareStatement(query);
				      preparedStmt.setString (1, violationMessage);
				      preparedStmt.setString (2, eventTriggeredBy);
				      preparedStmt.setString(3, ruleViolated);
				      preparedStmt.setLong(4, currentTimeMillis);
				      preparedStmt.setObject(5, metaData);
				      // execute the preparedstatement
				      preparedStmt.execute();
				      logger.info("Violation on rule " + ruleViolated + " stored.");
				      return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Failure on storing violation on db");
			return false;
		}
		return false;		
		
	}

//	public void saveWindData(ConcernAnemometerEvent<?> receivedEvent) {
//
//		try {
//			if (this.con != null && !this.con.isClosed()) {
//					 String query = " insert into anemometer (timest, windangle, windstrength, gustangle, guststrength)"
//						        + " values (?, ?, ?, ?, ?)";			
//				      // create the mysql insert preparedstatement
//				      PreparedStatement preparedStmt = this.con.prepareStatement(query);
//				      preparedStmt.setDouble (1, receivedEvent.getTimestamp());
//				      preparedStmt.setInt(2, receivedEvent.getWindAngle());
//				      preparedStmt.setInt(3, receivedEvent.getWindStrenght());
//				      preparedStmt.setInt(4, receivedEvent.getGustAngle());
//				      preparedStmt.setInt(5, receivedEvent.getGustStrength());
//				      // execute the preparedstatement
//				      preparedStmt.execute();
//				      logger.info("Anemometer Event" + receivedEvent.getName()+ " stored.");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			logger.error("Failure on storing violation on db");
//		}
//	}
}
