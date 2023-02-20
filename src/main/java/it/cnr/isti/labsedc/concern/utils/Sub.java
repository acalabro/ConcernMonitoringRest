package it.cnr.isti.labsedc.concern.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Sub {
    
	public Sub(final double op1, final double op2 ){
    	
    }
    
	public static String readFile(String filePath) {

		String text="";
		
		try {
		Path fileName = Path.of(filePath);

	    text = Files.readString(fileName, StandardCharsets.ISO_8859_1);
	    		} catch (IOException e) {
			e.printStackTrace();
		}
	    return text;

	}

	public static String newSessionLogger() {
	
		return "\n\n-----------------------------------------------------\n" 
			  +"- NEW MONITORING SESSION - ALL COMPONENTS RESTARTED -\n"
			  +"-----------------------------------------------------\n\n\n";
	}
	
	 public static void cleanFile(String fileName) {
	   		java.io.FileWriter fw;
	   		java.io.PrintWriter pw;
			try {
				fw = new java.io.FileWriter(fileName, false);
		   		pw = new java.io.PrintWriter(fw, false);
		   		pw.flush();
		   		pw.close();   	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	   }
}