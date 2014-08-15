package eu.ammw.msc.plaga.exec;

import eu.ammw.msc.plaga.common.Utils;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;


public class ExecBehaviour extends Behaviour {
	private short progress = 0;
	private boolean done = false;

	private static final String DOWNLOAD_DIR_PREFIX = "files";

	private ACLMessage message;
	private byte[] messageContent;
	private String id;
	private String fileName;

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
		this.messageContent = Base64.decodeBase64(message.getContent().getBytes());
		String path = null;
		id = String.valueOf(System.currentTimeMillis());
		fileName = "run.jar";
		try {
			path = DOWNLOAD_DIR_PREFIX + File.separator + id;
			Utils.createDirectory(path);
			path += File.separator + fileName;
			logger.info(path);
			Utils.writeFile(path, messageContent);
		} catch (IOException ioe) {
			logger.severe("Cannot write file: " + path);
			logger.severe(ioe.toString());
		}
		this.message = message;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if (++progress == 5) done = true;
		System.out.println(messageContent + " progress: " + progress);
	}

	@Override
	public boolean done() {
		return done;
	}

	public short getProgress() {
		return progress;
	}
}
