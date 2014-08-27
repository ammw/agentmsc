package eu.ammw.msc.plaga.exec;

import eu.ammw.msc.plaga.common.Utils;
import eu.ammw.msc.plaga.common.task.Task;
import eu.ammw.msc.plaga.common.task.TaskExecutable;
import eu.ammw.msc.plaga.exec.threading.TaskThread;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;

import java.io.IOException;
import java.net.MalformedURLException;


public class ExecBehaviour extends Behaviour {
	private boolean done = false;

	private Task task;
	private TaskThread thread;

	private Logger logger;

	public ExecBehaviour(ACLMessage message) {
		super();
		extractDataFromMessage(message);
	}

	public ExecBehaviour(ACLMessage message, Agent agent) {
		super(agent);
		extractDataFromMessage(message);
	}

	private void extractDataFromMessage(ACLMessage message) {
		logger = Logger.getJADELogger(myAgent == null ?
				ExecAgent.class.getName() : myAgent.getClass().getName());
		try {
			task = new Task((TaskExecutable) (message.getContentObject()));
			task.willExecute();
			Utils.createDirectory(task.getConfig().getDirectory());
			Utils.writeFile(task.getConfig().getPath(), task.getJar().getFileContent());
		} catch (UnreadableException ue) {
			logger.severe("Unreadable task!");
			logger.severe(ue.toString());
			// inform sender
			Utils.informNotUnderstood(myAgent, message);
			done = true;
		} catch (IOException ioe) {
			Utils.logStackTrace(ioe, logger);
			done = true;
		}
	}

	@Override
	public void action() {
		if (thread == null) {
			try {
				// execute
				logger.info("Starting task " + task.getId());
				logger.info("Directory for task " + task.getId() + " is " + task.getConfig().getDirectory());
				thread = new TaskThread(task);
				thread.run();
			} catch (MalformedURLException mue) {
				Utils.logStackTrace(mue, logger);
				done = true;
			}
		} else if (thread.isAlive()) {
			logger.info("Task " + task.getId() + " in progress, please wait...");
		} else {
			logger.info("Task " + task.getId() + " has finished.");
			done = true;
		}
	}

	@Override
	public boolean done() {
		return done;
	}
}
