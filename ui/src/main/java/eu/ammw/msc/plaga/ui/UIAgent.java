package eu.ammw.msc.plaga.ui;

import eu.ammw.msc.plaga.common.MessageType;
import eu.ammw.msc.plaga.common.ServiceType;
import eu.ammw.msc.plaga.common.Task;
import eu.ammw.msc.plaga.common.Utils;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.io.IOException;

/**
 * @author AMW
 */
public class UIAgent extends Agent {
	private Logger logger;

	@Override
	public void setup() {
		super.setup();
		logger = Logger.getJADELogger(getClass().getName());
		this.addBehaviour(new Behaviour() {
			private int left = (getArguments() == null) ? 0 : getArguments().length;

			@Override
			public boolean done() {
				if (left < 1) doDelete();
				return left < 1;
			}

			@Override
			public void action() {
				if (getArguments() == null) {
					left = 0;
					return;
				}
				String path = getArguments()[--left].toString();
				// read file and send to exec
				try {
					long timestamp = System.currentTimeMillis();
					Task task = new Task(myAgent.getLocalName() + timestamp, Utils.readFile(path), "eu.ammw.msc.test.SecurityTest", null);
					task.setJarFileName("main" + timestamp + ".jar");

					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.setContentObject(task);
					msg.setConversationId(myAgent.getLocalName() + timestamp);
					msg.addUserDefinedParameter(MessageType.MESSAGE_TYPE.toString(), MessageType.TASK_CREATE.toString());

					// Find EXEC in directory
					AID[] execs = Utils.getAgentsForServiceType(myAgent, ServiceType.EXEC);
					if (execs == null || execs.length == 0) {
						left++;
						logger.info("Found no one to do the task. Will retry in 15s.");
						try {
							Thread.sleep(15000);
						} catch (InterruptedException e1) {
							logger.severe(e1.toString());
							doDelete();
						}
					} else {
						msg.addReceiver(execs[0]);
						msg.addReplyTo(getAID());
						send(msg);
					}
				} catch (IOException ioe) {
					logger.severe("Could not read file: " + path);
					logger.severe(ioe.toString());
				}
			}
		});

		logger.info(getLocalName() + " started.");
	}

	@Override
	public void takeDown() {
		super.takeDown();
		logger.warning(getLocalName() + " terminated!");
	}
}
