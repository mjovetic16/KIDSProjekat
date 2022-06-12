package app;

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
import java.util.List;
import java.util.Scanner;

public class ServentInitializer implements Runnable {

	private ServentInfo getSomeServentPort() {
		int bsPort = AppConfig.getBootstrapNode().getListenerPort();
		
		int retVal = -2;

		ServentInfo info = new ServentInfo("",1000,0,new ArrayList<>());
		
		try {
			Socket bsSocket = new Socket(AppConfig.getBootstrapNode().getIpAddress(), bsPort);
			
			PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
			bsWriter.write("Hail\n" + AppConfig.myServentInfo.getListenerPort()+"\n"+ AppConfig.myServentInfo.getIpAddress() + "\n");
			bsWriter.flush();
			
			Scanner bsScanner = new Scanner(bsSocket.getInputStream());
			int port = bsScanner.nextInt();
			String ipAddress = bsScanner.nextLine();
			info = new ServentInfo(ipAddress, info.getId(), port,info.getNeighbors());

			bsSocket.close();
		} catch (Exception e){

			errorLog("Error",e);
		}
		
		return info;
	}
	
	@Override
	public void run() {

		ServentInfo info = getSomeServentPort();
		int someServentPort = info.getListenerPort();
		String ip = info.getIpAddress();
		
		if (someServentPort == -2) {
			AppConfig.timestampedErrorPrint("Error in contacting bootstrap. Exiting...");
			System.exit(0);
		}
		if (someServentPort == -1) { //bootstrap gave us -1 -> we are first
			AppConfig.timestampedStandardPrint("First node in system.");

			List<ServentInfo> neigbors = new ArrayList<>();
			neigbors.add(AppConfig.myServentInfo);
			AppConfig.setNeighbors(neigbors);
			AppConfig.setServentCount(1);


		} else { //bootstrap gave us something else - let that node tell our successor that we are here


			ServentInfo serventInfo = new ServentInfo(ip,10000,someServentPort,new ArrayList<>());
			NewNodeMessage nnm = new NewNodeMessage(AppConfig.myServentInfo, serventInfo);
			MessageUtil.sendMessage(nnm);

			log("Sending new node message to: "+nnm.getReceiverInfo());
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
