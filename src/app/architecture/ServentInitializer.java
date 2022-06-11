package app.architecture;

import app.AppConfig;
import app.ServentInfo;
import servent.message.types.NewNodeMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ServentInitializer implements Runnable {

	private int getSomeServentPort() {
		int bsPort = AppConfig.getBootstrapNode().getListenerPort();
		
		int retVal = -2;
		
		try {
			Socket bsSocket = new Socket(AppConfig.getBootstrapNode().getIpAddress(), bsPort);
			
			PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
			bsWriter.write("Hail\n" + AppConfig.myServentInfo.getListenerPort() + "\n");
			bsWriter.flush();
			
			Scanner bsScanner = new Scanner(bsSocket.getInputStream());
			retVal = bsScanner.nextInt();
			
			bsSocket.close();
		} catch (Exception e){

			errorLog("Error",e);
		}
		
		return retVal;
	}
	
	@Override
	public void run() {
		int someServentPort = getSomeServentPort();
		
		if (someServentPort == -2) {
			AppConfig.timestampedErrorPrint("Error in contacting bootstrap. Exiting...");
			System.exit(0);
		}
		if (someServentPort == -1) { //bootstrap gave us -1 -> we are first
			AppConfig.timestampedStandardPrint("First node in system.");
		} else { //bootstrap gave us something else - let that node tell our successor that we are here
			ServentInfo serventInfo = new ServentInfo("empty",10000,someServentPort,new ArrayList<>());
			NewNodeMessage nnm = new NewNodeMessage(AppConfig.myServentInfo, serventInfo);
			MessageUtil.sendMessage(nnm);
		}
	}


	public void log(String s){

		AppConfig.timestampedStandardPrint("[ServentInitializer]: "+s);
	}

	public void errorLog(String s, Exception e){

		AppConfig.timestampedErrorPrint("[ServentInitializer]: "+s);
		AppConfig.timestampedErrorPrint("[ServentInitializer]: "+e.toString());
		AppConfig.timestampedErrorPrint("[ServentInitializer]: "+ Arrays.toString(e.getStackTrace()));
	}

}
