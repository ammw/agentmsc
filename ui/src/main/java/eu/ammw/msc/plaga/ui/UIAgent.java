package eu.ammw.msc.plaga.ui;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

/**
 * @author AMW
 *
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
				return left<1;
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
				// TODO directory
				msg.addReceiver(new AID("exec0", AID.ISLOCALNAME));
				msg.addReplyTo(getAID());
				send(msg);
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
