package eu.ammw.msc.plaga.ui;

import eu.ammw.msc.plaga.common.ServiceType;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

/**
 * @author AMW
 */
public class UIAgent extends Agent {
	private Logger logger;

	@Override
	public void setup() {
		super.setup();
		logger = Logger.getJADELogger(this.getLocalName());
		this.addBehaviour(new Behaviour() {
			private int left = (getArguments() == null) ? 0 : getArguments().length;

			@Override
			public boolean done() {
				return left < 1;
			}

			@Override
			public void action() {
				if (getArguments() == null) {
					left = 0;
					return;
				}
				String path = getArguments()[--left].toString();
				// TODO read files :)
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setContent(path);

				// Find EXEC in directory
				ServiceDescription service = new ServiceDescription();
				service.setType(ServiceType.EXEC.toString());
				DFAgentDescription template = new DFAgentDescription();
				template.addServices(service);
				AID chosenOne = null;
				try {
					chosenOne = DFService.search(myAgent, template)[0].getName();
				} catch (Exception e) {
					logger.info("Found no one to do the task. Will retry in 15s.");
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e1) {
						logger.severe(e1.toString());
						doDelete();
					}
				}

				if (chosenOne == null)
					left++;
				else {
					msg.addReceiver(chosenOne);
					msg.addReplyTo(getAID());
					send(msg);
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
