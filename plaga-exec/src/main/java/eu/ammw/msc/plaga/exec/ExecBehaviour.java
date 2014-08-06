package eu.ammw.msc.plaga.exec;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import org.apache.commons.codec.binary.Base64;

public class ExecBehaviour extends Behaviour {
	private short progress = 0;
	private boolean done = false;
	
	public ExecBehaviour() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExecBehaviour(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if (++progress == 10000) done = true;
	}

	@Override
	public boolean done() {
		return done;
	}
	
	public short getProgress() {
		return progress;
	}
}
