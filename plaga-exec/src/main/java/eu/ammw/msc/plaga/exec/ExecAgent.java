package eu.ammw.msc.plaga.exec;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 * @author AMW
 *
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
				ACLMessage message = myAgent.receive(execMessage);
				if (message == null)
					block();
				else {
					myAgent.addBehaviour(new ExecBehaviour(message));
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
