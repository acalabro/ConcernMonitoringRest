package it.cnr.isti.labsedc.concern.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.cnr.isti.labsedc.concern.event.Event;

public class MySQLStorageController implements StorageController {

    private static final Logger logger = LogManager.getLogger(MySQLStorageController.class);
    
	public String serverAddress = "localhost"; //default is localhost
	public int serverPort = 3306; //default is mysql
	public String username = "concern";
	public String password = "unsecure";
	public String dbName = "concern";
	private static Connection con;
    
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
				Class.forName("com.mysql.jdbc.Driver");  
				con=DriverManager.getConnection(  
				"jdbc:mysql://"+serverAddress+":"+serverPort+"/"+dbName,username,password);
				logger.info("Connected successfully to "+dbName+" on "+serverAddress);
				return true;
			}
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
			 String query = " insert into event (senderID, timestamp, data, dataClassName  )"
				        + " values (?, ?, ?, ?)";
	
				      // create the mysql insert preparedstatement
				      PreparedStatement preparedStmt = MySQLStorageController.con.prepareStatement(query);
				      preparedStmt.setString (1, message.getSenderID());
				      preparedStmt.setLong (2, message.getTimestamp());
				      preparedStmt.setObject(3, message.getData());
				      preparedStmt.setString(4, message.getData().getClass().getCanonicalName());
				      // execute the preparedstatement
				      preparedStmt.execute();
				      return true;
			}
			catch (SQLException e) {
				logger.warn("Failure on storing event on db");
				return false;
		}
	}

}
