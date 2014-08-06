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
	private MessageTemplate execRequestMessage;
	
	@Override
	public void setup() {
		super.setup();
		logger = Logger.getJADELogger(this.getLocalName());
		
		execRequestMessage = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
		// wait for tasks
		this.addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				ACLMessage message = myAgent.receive(execRequestMessage);
				if (message == null)
					block();
				else {
					//
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
