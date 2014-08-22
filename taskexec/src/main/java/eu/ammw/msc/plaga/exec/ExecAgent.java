package eu.ammw.msc.plaga.exec;

import eu.ammw.msc.plaga.common.MessageType;
import eu.ammw.msc.plaga.common.ServiceType;
import eu.ammw.msc.plaga.common.Utils;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 * @author AMW
 */
public class ExecAgent extends Agent {
	private Logger logger;
	private MessageTemplate execMessage;

	@Override
	public void setup() {
		super.setup();
		logger = Logger.getJADELogger(getClass().getName());

		ACLMessage tempMessage = new ACLMessage(ACLMessage.REQUEST);
		tempMessage.addUserDefinedParameter(MessageType.MESSAGE_TYPE.toString(), MessageType.TASK_CREATE.toString());
		execMessage = MessageTemplate.MatchCustom(tempMessage, true);

		// wait for tasks
		this.addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				// TODO negotiations
				logger.fine("Gotcha! " + myAgent.getName());
				ACLMessage message = myAgent.receive(execMessage);
				if (message == null)
					block();
				else {
					myAgent.addBehaviour(new ExecBehaviour(message));
				}
			}
		});

		// Register EXEC service
		if (!Utils.registerService(this, ServiceType.EXEC))
			doDelete();

		logger.info(getLocalName() + " started.");
	}

	@Override
	public void takeDown() {
		super.takeDown();
		Utils.unregisterService(this);
		logger.warning(getLocalName() + " terminated!");
	}
}
