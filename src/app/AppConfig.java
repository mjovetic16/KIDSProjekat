package app;

import app.models.*;
import app.models.job.ActiveJob;
import app.models.job.Dot;
import app.models.job.Job;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contains all the global application configuration stuff.
 * @author bmilojkovic
 *
 */

//Ovo je prazan projekat, koji moze da vam koristi kao dobra pocetna tacka za projekat :D
public class AppConfig {

	/**
	 * Convenience access for this servent's information
	 */
	public static ServentInfo myServentInfo;
	
	private static List<ServentInfo> serventInfoList = new ArrayList<>();


	private static List<Job> jobList = new ArrayList<>();

	private static BootstrapNode bootstrapNode = new BootstrapNode();

	private static Fail fail = new Fail();

	private static ActiveJob activeJob;

	private static final String jobLock = "jobLock";


	/**
	 * If this is true, the system is a clique - all nodes are each other's
	 * neighbors. 
	 */
	public static boolean IS_CLIQUE;
	
	/**
	 * Print a message to stdout with a timestamp
	 * @param message message to print
	 */
	public static void timestampedStandardPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.out.println(timeFormat.format(now) + " - " + message);
	}
	
	/**
	 * Print a message to stderr with a timestamp
	 * @param message message to print
	 */
	public static void timestampedErrorPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.err.println(timeFormat.format(now) + " - " + message);
	}
	
	/**
	 * Reads a config file. Should be called once at start of app.
	 * The config file should be of the following format:
	 * <br/>
	 * <code><br/>
	 * servent_count=3 			- number of servents in the system <br/>
	 * clique=false 			- is it a clique or not <br/>
	 * fifo=false				- should sending be fifo
	 * servent0.port=1100 		- listener ports for each servent <br/>
	 * servent1.port=1200 <br/>
	 * servent2.port=1300 <br/>
	 * servent0.neighbors=1,2 	- if not a clique, who are the neighbors <br/>
	 * servent1.neighbors=0 <br/>
	 * servent2.neighbors=0 <br/>
	 * 
	 * </code>
	 * <br/>
	 * So in this case, we would have three servents, listening on ports:
	 * 1100, 1200, and 1300. This is not a clique, and:<br/>
	 * servent 0 sees servent 1 and 2<br/>
	 * servent 1 sees servent 0<br/>
	 * servent 2 sees servent 0<br/>
	 * 
	 * @param configName name of configuration file
	 */
	public static void readConfig(String configName){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(configName)));
			
		} catch (IOException e) {
			timestampedErrorPrint("Couldn't open properties file. Exiting...");
			System.exit(0);
		}
		
		int serventCount = -1;
		try {
			serventCount = Integer.parseInt(properties.getProperty("servent_count"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading servent_count. Exiting...");
			System.exit(0);
		}
		
		IS_CLIQUE = Boolean.parseBoolean(properties.getProperty("clique", "false"));
		String snapshotType = properties.getProperty("snapshot");
		if (snapshotType == null) {
			snapshotType = "none";
		}
		
		for (int i = 0; i < serventCount; i++) {
			String portProperty = "servent"+i+".port";
			
			int serventPort = -1;
			
			try {
				serventPort = Integer.parseInt(properties.getProperty(portProperty));
			} catch (NumberFormatException e) {
				timestampedErrorPrint("Problem reading " + portProperty + ". Exiting...");
				System.exit(0);
			}
			
			List<Integer> neighborList = new ArrayList<>();
			if (IS_CLIQUE) {
				for(int j = 0; j < serventCount; j++) {
					if (j == i) {
						continue;
					}
					
					neighborList.add(j);
				}
			} else {
				String neighborListProp = properties.getProperty("servent"+i+".neighbors");
				
				if (neighborListProp == null) {
					timestampedErrorPrint("Warning: graph is not clique, and node " + i + " doesnt have neighbors");
				} else {
					String[] neighborListArr = neighborListProp.split(",");
					
					try {
						for (String neighbor : neighborListArr) {
							neighborList.add(Integer.parseInt(neighbor));
						}
					} catch (NumberFormatException e) {
						timestampedErrorPrint("Bad neighbor list for node " + i + ": " + neighborListProp);
					}
				}
			}
			
			ServentInfo newInfo = new ServentInfo("localhost", i, serventPort, neighborList);
			serventInfoList.add(newInfo);
		}




		try {
			fail.setStrong(Integer.parseInt(properties.getProperty("fail.strong")));
			fail.setWeak(Integer.parseInt(properties.getProperty("fail.weak")));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading fail. Exiting...");
			System.exit(0);
		}

		try {
			bootstrapNode.setIpAddress(properties.getProperty("bootstrap.ip"));
			bootstrapNode.setListenerPort(Integer.parseInt(properties.getProperty("bootstrap.port")));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading bootstrap node. Exiting...");
			System.exit(0);
		}

		int jobCount = -1;
		try {
			jobCount = Integer.parseInt(properties.getProperty("job_count"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading job_count. Exiting...");
			System.exit(0);
		}

		for (int i = 1; i <= jobCount; i++) {
			Job job = new Job();

			String jobName = "job"+i;
			String name = "";
			int n = 0;
			double p = 0;
			int w = 0;
			int h = 0;
			String  dots = "";


			try {

				name = properties.getProperty(jobName+".name");
				n = Integer.parseInt(properties.getProperty(jobName+".n"));
				p = Double.parseDouble(properties.getProperty(jobName+".p"));
				w = Integer.parseInt(properties.getProperty(jobName+".w"));
				h = Integer.parseInt(properties.getProperty(jobName+".h"));
				dots = properties.getProperty(jobName+".A");

			} catch (NumberFormatException e) {
				timestampedErrorPrint("Problem reading " + jobName + ". Exiting...");
				System.exit(0);
			}


			HashMap<String, Dot> dotMap = new HashMap<>();
			try{

				String[] dArray = dots.split("/");

				for(String s: dArray){
					Dot d = new Dot();
					s = s.replaceAll("\\(","");
					s = s.replaceAll("\\)","");
					String [] sArr = s.split(",");
					d.setX(Integer.parseInt(sArr[0]));
					d.setY(Integer.parseInt(sArr[1]));
					dotMap.put(d.toStringSimplified(),d);
				}

			} catch (Exception e){
				timestampedErrorPrint("Problem reading dots" + dots + ". Exiting...");
				timestampedErrorPrint(e.toString());
				System.exit(0);
			}

			job.setName(name);
			job.setN(n);
			job.setP(p);
			job.setW(w);
			job.setH(h);
			job.setA(dotMap);

			jobList.add(job);
		}


//		timestampedStandardPrint(jobList.toString());
//		timestampedStandardPrint(fail.toString());
//		timestampedStandardPrint(bootstrapNode.toString());

	}
	
	/**
	 * Get info for a servent selected by a given id.
	 * @param id id of servent to get info for
	 * @return {@link ServentInfo} object for this id
	 */
	public static ServentInfo getInfoById(int id) {
		if (id >= getServentCount()) {
			throw new IllegalArgumentException(
					"Trying to get info for servent " + id + " when there are " + getServentCount() + " servents.");
		}
		return serventInfoList.get(id);
	}
	
	/**
	 * Get number of servents in this system.
	 */
	public static int getServentCount() {
		return serventInfoList.size();
	}

	public String getJobLock(){
		return jobLock;
	}

	public static ActiveJob getActiveJob(){
		synchronized (jobLock){
			return activeJob;
		}
	}

	public static void setActiveJob(ActiveJob activeJob){
		synchronized (jobLock){
			AppConfig.activeJob = activeJob;
		}
	}

	public static List<Job> getJobList() {
		return jobList;
	}
}
