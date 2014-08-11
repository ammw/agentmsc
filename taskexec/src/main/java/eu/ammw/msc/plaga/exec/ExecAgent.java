package eu.ammw.msc.plaga.exec;

import eu.ammw.msc.plaga.common.ServiceType;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
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
		logger = Logger.getJADELogger(this.getLocalName());

		execMessage = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

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
		DFAgentDescription description = new DFAgentDescription();
		description.setName(getAID());
		ServiceDescription service = new ServiceDescription();
		service.setType(ServiceType.EXEC.toString());
		service.setName(getClass().getName() + "_" + getLocalName());
		description.addServices(service);
		try {
			DFService.register(this, description);
		} catch (FIPAException e) {
			logger.severe("Could not register EXEC agent " + getName());
			logger.severe(e.toString());
			doDelete();
		}

		logger.info(getLocalName() + " started.");
	}

	@Override
	public void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			logger.warning("Could not unregister " + getName());
			logger.warning(e.toString());
		}
		logger.warning(getLocalName() + " terminated!");
	}
}
