package eu.ammw.msc.plaga.exec;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import org.apache.commons.codec.binary.Base64;


public class ExecBehaviour extends Behaviour {
	private short progress = 0;
	private boolean done = false;

	private ACLMessage message;
	private String messageContent;

	public ExecBehaviour(ACLMessage message) {
		super();
		extractDataFromMessage(message);
	}

	public ExecBehaviour(ACLMessage message, Agent agent) {
		super(agent);
		extractDataFromMessage(message);
	}

	private void extractDataFromMessage(ACLMessage message) {
		System.err.println(message.getContent());
		this.messageContent = new String(Base64.decodeBase64(message.getContent().getBytes()));
		System.err.println(messageContent);
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
