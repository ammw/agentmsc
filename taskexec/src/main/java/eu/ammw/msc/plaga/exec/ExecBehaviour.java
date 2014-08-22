package eu.ammw.msc.plaga.exec;

import eu.ammw.msc.plaga.common.Task;
import eu.ammw.msc.plaga.common.Utils;
import eu.ammw.msc.plaga.exec.threading.TaskThread;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;

import java.io.IOException;


public class ExecBehaviour extends Behaviour {
	private short progress = 0;
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
			task = (Task) (message.getContentObject());
			Utils.createDirectory(task.getDirectory());
			Utils.writeFile(task.getPath(), task.getFileContent());
			// execute
			thread = new TaskThread(task);
			thread.run();
		} catch (UnreadableException ue) {
			logger.severe("Unreadable task!");
			logger.severe(ue.toString());
			// inform sender
			Utils.informNotUnderstood(myAgent, message);
			progress = Short.MAX_VALUE;
		} catch (IOException ioe) {
			Utils.logStackTrace(ioe, logger);
			progress = Short.MAX_VALUE;
		}
	}

	@Override
	public void action() {
		if (progress++ > 5) done = true;
		logger.info(task.getId() + " progress: " + progress);
	}

	@Override
	public boolean done() {
		return done;
	}

	public short getProgress() {
		return progress;
	}
}
