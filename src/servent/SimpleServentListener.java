package servent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.AppConfig;
import app.Cancellable;
import app.manager.JobManager;
import servent.handler.*;
import servent.message.Message;
import servent.message.util.MessageUtil;

public class SimpleServentListener implements Runnable, Cancellable {

	private volatile boolean working = true;

	private JobManager jobManager;
	
	public SimpleServentListener(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	/*
	 * Thread pool for executing the handlers. Each client will get it's own handler thread.
	 */
	private final ExecutorService threadPool = Executors.newWorkStealingPool();
	
	@Override
	public void run() {
		ServerSocket listenerSocket = null;
		try {
			listenerSocket = new ServerSocket(AppConfig.myServentInfo.getListenerPort(), 100);
			/*
			 * If there is no connection after 1s, wake up and see if we should terminate.
			 */
			listenerSocket.setSoTimeout(1000);
		} catch (IOException e) {
			AppConfig.timestampedErrorPrint("Couldn't open listener socket on: " + AppConfig.myServentInfo.getListenerPort());
			System.exit(0);
		}
		
		
		while (working) {
			try {
				Message clientMessage;
				
				Socket clientSocket = listenerSocket.accept();
				
				//GOT A MESSAGE! <3
				clientMessage = MessageUtil.readMessage(clientSocket);
				
				MessageHandler messageHandler = new NullHandler(clientMessage);
				
				/*
				 * Each message type has it's own handler.
				 * If we can get away with stateless handlers, we will,
				 * because that way is much simpler and less error prone.
				 */
//				log("Got message type: "+clientMessage.getMessageType());
				switch (clientMessage.getMessageType()) {

					case JOB_REQUEST :
						messageHandler = new JobRequestMessageHandler(clientMessage, jobManager);
						break;
					case JOB_RESPONSE :
						messageHandler = new JobResponseMessageHandler(clientMessage, jobManager);
						break;
					case RESULT :
						messageHandler = new ResultMessageHandler(clientMessage, jobManager);
						break;
					case NEW_NODE:
						messageHandler = new NewNodeMessageHandler(clientMessage);
						break;
					case WELCOME:
						messageHandler = new WelcomeMessageHandler(clientMessage);
						break;
					case UPDATE:
						messageHandler = new UpdateMessageHandler(clientMessage);
						break;
					case STOP:
						messageHandler = new StopMessageHandler(clientMessage, jobManager);
						break;
					case STATUS:
						messageHandler = new StatusMessageHandler(clientMessage, jobManager);
						break;
				}
				
				threadPool.submit(messageHandler);
			} catch (SocketTimeoutException timeoutEx) {
				//Uncomment the next line to see that we are waking up every second.
//				AppConfig.timedStandardPrint("Waiting...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		this.working = false;
	}


	public void log(String s){

		AppConfig.timestampedStandardPrint("[SSListener]: "+s);
	}

	public void errorLog(String s, Exception e){

		AppConfig.timestampedErrorPrint("[SSListener]: "+s);
		AppConfig.timestampedErrorPrint("[SSListener]: "+e.toString());
		AppConfig.timestampedErrorPrint("[SSListener]: "+ Arrays.toString(e.getStackTrace()));
	}

}
