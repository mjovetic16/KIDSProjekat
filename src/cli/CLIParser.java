package cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import app.AppConfig;
import app.Cancellable;
import app.manager.JobManager;
import cli.command.*;
import servent.SimpleServentListener;

/**
 * A simple CLI parser. Each command has a name and arbitrary arguments.
 * 
 * Currently supported commands:
 * 
 * <ul>
 * <li><code>info</code> - prints information about the current node</li>
 * <li><code>pause [ms]</code> - pauses exection given number of ms - useful when scripting</li>
 * <li><code>ping [id]</code> - sends a PING message to node [id] </li>
 * <li><code>broadcast [text]</code> - broadcasts the given text to all nodes</li>
 * <li><code>causal_broadcast [text]</code> - causally broadcasts the given text to all nodes</li>
 * <li><code>print_causal</code> - prints all received causal broadcast messages</li>
 * <li><code>stop</code> - stops the servent and program finishes</li>
 * </ul>
 * 
 * @author bmilojkovic
 *
 */
public class CLIParser implements Runnable, Cancellable {

	private volatile boolean working = true;
	
	private final List<CLICommand> commandList;
	
	public CLIParser(SimpleServentListener listener, JobManager jobManager) {
		this.commandList = new ArrayList<>();
		
		commandList.add(new InfoCommand());
		commandList.add(new PauseCommand());
		commandList.add(new StopCommand(this, listener));

		commandList.add(new StartCommand(jobManager));
		commandList.add(new StatusCommand(jobManager));
		commandList.add(new ResultCommand(jobManager));
		commandList.add(new StopFractalCommand(jobManager));
		commandList.add(new QuitCommand(this,listener,jobManager));
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		
		while (working) {
			String commandLine = sc.nextLine();
			
			int spacePos = commandLine.indexOf(" ");
			
			String commandName = null;
			String commandArgs = null;
			if (spacePos != -1) {
				commandName = commandLine.substring(0, spacePos);
				commandArgs = commandLine.substring(spacePos+1, commandLine.length());
			} else {
				commandName = commandLine;
			}
			
			boolean found = false;
			
			for (CLICommand cliCommand : commandList) {
				if (cliCommand.commandName().equals(commandName)) {
					//log(commandName+"|"+cliCommand.commandName());
					cliCommand.execute(commandArgs);
					found = true;
					break;
				}
			}
			
			if (!found) {
				AppConfig.timestampedErrorPrint("Unknown command: " + commandName);
			}
		}
		
		sc.close();
	}
	
	@Override
	public void stop() {
		this.working = false;
		
	}

	public void log(String s){

		AppConfig.timestampedStandardPrint("[CLIParser]: "+s);
	}

	public void errorLog(String s, Exception e){

		AppConfig.timestampedErrorPrint("[CLIParser]: "+s);
		AppConfig.timestampedErrorPrint("[CLIParser]: "+e.toString());
		AppConfig.timestampedErrorPrint("[CLIParser]: "+ Arrays.toString(e.getStackTrace()));
	}
}
