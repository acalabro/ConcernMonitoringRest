package it.cnr.isti.labsedc.concern.notification;

import java.io.OutputStream;

import com.fazecast.jSerialComm.SerialPort;

public class LoRaOnSerialWriter {

	String port;
	SerialPort comPort;
	OutputStream out;
	public LoRaOnSerialWriter() {
	}
	
	public void connect(String port) {
		SerialPort[] comPorts = SerialPort.getCommPorts();
		for (int i = 0;i<comPorts.length; i++) {
			if (comPorts[i].getSystemPortName().compareTo(port) == 0) {
				comPort = comPorts[i];
				comPort.openPort();
				comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);			
			}
		}
	}

	public void write(String message) {
		try
		{
			out = comPort.getOutputStream();
			out.write(message.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void closePort() {
		comPort.closePort();
	}
	
	public static void main(String[] args) {
		//test
		LoRaOnSerialWriter writer = new LoRaOnSerialWriter();
		writer.connect("ttyUSB0");
		writer.write("##1,4,42.9885541,10.5951233##");
		writer.closePort();
	}
}
