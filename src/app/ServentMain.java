package app;

import app.manager.JobManager;
import cli.CLIParser;
import servent.SimpleServentListener;

import java.util.Arrays;

/**
 * Describes the procedure for starting a single Servent
 *
 * @author bmilojkovic
 */
public class ServentMain {

	/**
	 * Command line arguments are:
	 * 0 - path to servent list file
	 * 1 - this servent's id
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			AppConfig.timestampedErrorPrint("Please provide servent list file and id of this servent.");
		}
		
		int serventId = -1;
		int portNumber = -1;
		
		String serventListFile = args[0];
		
		AppConfig.readConfig(serventListFile);
		
		try {
			serventId = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Second argument should be an int. Exiting...");
			System.exit(0);
		}
		
		if (serventId >= AppConfig.getInitServentCount()) {
			AppConfig.timestampedErrorPrint("Invalid servent id provided");
			System.exit(0);
		}
		
		AppConfig.myServentInfo = AppConfig.getInfoById(serventId);
		
		try {
			portNumber = AppConfig.myServentInfo.getListenerPort();
			
			if (portNumber < 1000 || portNumber > 2000) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Port number should be in range 1000-2000. Exiting...");
			System.exit(0);
		}
		
		AppConfig.timestampedStandardPrint("Starting servent " + AppConfig.myServentInfo);

		JobManager jobManager = new JobManager();
		Thread jobManagerThread = new Thread(jobManager);
		jobManagerThread.start();
		
		SimpleServentListener simpleListener = new SimpleServentListener(jobManager);
		Thread listenerThread = new Thread(simpleListener);
		listenerThread.start();

		ServentInitializer serventInitializer = new ServentInitializer();
		Thread initializerThread = new Thread(serventInitializer);
		initializerThread.start();
		
		CLIParser cliParser = new CLIParser(simpleListener, jobManager);
		Thread cliThread = new Thread(cliParser);
		cliThread.start();
		
	}


	public void log(String s){

		AppConfig.timestampedStandardPrint("[ServentMain]: "+s);
	}

	public void errorLog(String s, Exception e){

		AppConfig.timestampedErrorPrint("[ServentMain]: "+s);
		AppConfig.timestampedErrorPrint("[ServentMain]: "+e.toString());
		AppConfig.timestampedErrorPrint("[ServentMain]: "+ Arrays.toString(e.getStackTrace()));
	}
}
