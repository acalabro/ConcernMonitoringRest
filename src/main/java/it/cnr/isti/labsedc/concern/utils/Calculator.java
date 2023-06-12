package it.cnr.isti.labsedc.concern.utils;

public class Calculator {
	
	public static long latency(long eventOne, long eventTwo) {
		return eventOne - eventTwo;
	}
	
	public static boolean checkIfItIsGreater(Object numberToCheck, int threshold) {
		if (Integer.valueOf((String)numberToCheck)>threshold)
			return true;
		return false;
	}
}
