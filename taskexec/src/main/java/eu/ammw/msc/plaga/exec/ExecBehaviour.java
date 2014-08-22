package eu.ammw.msc.plaga.exec;

import eu.ammw.msc.plaga.common.Task;
import eu.ammw.msc.plaga.common.Utils;
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
			// TODO execute
		} catch (UnreadableException ue) {
			logger.severe("Unreadable task!");
			logger.severe(ue.toString());
			// TODO inform sender
		} catch (IOException ioe) {
			logger.severe("Cannot write file: " + task.getPath());
			logger.severe(ioe.toString());
		}
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if (++progress == 5) done = true;
		System.out.println(task.getId() + " progress: " + progress);
	}

	@Override
	public boolean done() {
		return done;
	}

	public short getProgress() {
		return progress;
	}
}
