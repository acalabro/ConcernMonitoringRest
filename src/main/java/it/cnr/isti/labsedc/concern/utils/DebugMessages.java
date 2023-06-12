package it.cnr.isti.labsedc.concern.utils;

import java.util.Calendar;

/**
 * This class provides print function for debug
 * 
 * @author Antonello Calabr&ograve;
 *
 */
public class DebugMessages {

	public static int lastMessageLength = 0;
	public static Calendar calendarConverter = Calendar.getInstance();
	public static Long lastMessageTime = 0L;
	
	/**
	 * Print the string "className : message " without break line.
	 * Can be used with method {@link #ok()}
	 * 
	 * @param callerClass the name of the class that is calling method
	 * @param messageToPrint the message to print
	 */
	
	public static void print(Long now, String callerClass, String messageToPrint)
	{
		calendarConverter.setTimeInMillis(now);
		String message =  calendarConverter.getTime().toString() + " - " +  callerClass + ": " + messageToPrint;
		System.err.print(message);
		lastMessageLength = message.length();
		DebugMessages.lastMessageTime = now;
	}

	/**
	 * Print the string "className : message " with break line.
	 * Can be used with method {@link #ok()}
	 * 
	 * @param callerClass the name of the class that is calling method
	 * @param messageToPrint the message to print
	 */
	public static void println(Long now, String callerClass, String messageToPrint)
	{		
		calendarConverter.setTimeInMillis(now);
		String message =  calendarConverter.getTime().toString() + " - " +  callerClass + ": " + messageToPrint;
		System.err.println(message);
		DebugMessages.lastMessageTime = now;		
	}
	
	public static void fail()
	{
		int tab = 10 - (lastMessageLength / 8);
		String add="";
		for(int i = 0; i< tab;i++) {
			add +="\t"; 
		}
		System.out.println(add + "[FAIL]");
	}
	
	/**
	 * Print the OK text
	 */
	public static void ok()
	{
		int tab = 15 - (lastMessageLength / 8);
		String add="";
		for(int i = 0; i< tab;i++) {
			add +="\t"; 
		}
		System.err.println(add + "[ OK ]");
	}
	/**
	 * 
	 * Print a line <br />
	 */
	public static void line() {
		System.err.println("------------------------------------------------------------------------------------------------------------------------------");
	}
	
	/**
	 * Print asterisks
	 */
	public static void asterisks() {
		System.err.println("******************************************************************************************************************************");
	}
	public static void error(long currentTimeMillis, String callerClass, String messageToPrint) {

		System.err.println("******************************************************************************************************************************");
		System.err.println("------------------------------------------------------------------------------------------------------------------------------");
		calendarConverter.setTimeInMillis(currentTimeMillis);
		String message =  "[ERROR]" + calendarConverter.getTime().toString() + " - " +  callerClass + ": " + messageToPrint;
		System.err.print(message);
		lastMessageLength = message.length();
		DebugMessages.lastMessageTime = currentTimeMillis;
		System.err.println("------------------------------------------------------------------------------------------------------------------------------");
		System.err.println("******************************************************************************************************************************");
	}

}