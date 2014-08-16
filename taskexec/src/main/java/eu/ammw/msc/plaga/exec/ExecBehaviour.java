package eu.ammw.msc.plaga.exec;

import eu.ammw.msc.plaga.common.Task;
import eu.ammw.msc.plaga.common.Utils;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
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
		task = new Task(message.getContent());
		try {
			Utils.createDirectory(task.getDirectory());
			logger.info(task.getPath());
			Utils.writeFile(task.getPath(), task.getFileContent());
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
