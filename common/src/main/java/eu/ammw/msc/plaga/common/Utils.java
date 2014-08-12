package eu.ammw.msc.plaga.common;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.util.Logger;

/**
 * Utility class containing some potentially useful methods.
 *
 * @author AMW
 */
public abstract class Utils {
	/**
	 * Registers in DF a service for agent by type.
	 *
	 * @param agent       agent offering service (usually invoked with <code>this</code>)
	 * @param serviceType one of available service types
	 * @return true for success, false otherwise
	 */
	public static boolean registerService(Agent agent, ServiceType serviceType) {
		DFAgentDescription description = new DFAgentDescription();
		description.setName(agent.getAID());
		ServiceDescription service = new ServiceDescription();
		service.setType(serviceType.toString());
		service.setName(agent.getClass().getName() + "_" + agent.getLocalName());
		description.addServices(service);
		try {
			DFService.register(agent, description);
		} catch (FIPAException e) {
			Logger logger = Logger.getJADELogger(agent.getLocalName());
			logger.severe("Could not register " + serviceType.getDescription() + " agent " + agent.getName());
			logger.severe(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * Unegisters in DF a service for agent.
	 *
	 * @param agent agent offering service (usually invoked with <code>this</code>)
	 * @return true for success, false otherwise
	 */
	public static boolean unregisterService(Agent agent) {
		try {
			DFService.deregister(agent);
		} catch (FIPAException e) {
			Logger logger = Logger.getJADELogger(agent.getLocalName());
			logger.warning("Could not unregister " + agent.getName());
			logger.warning(e.toString());
		}
		return true;
	}

	/**
	 * Finds in DF all agents registered for given service.
	 *
	 * @param caller agent calling this method (usually <code>this</code> or <code>myAgent</code>)
	 * @param serviceType requested service type
	 * @return array of agent AIDs; empty when no agents present, <code>null</code> on error
	 */
	public static AID[] getAgentsForServiceType(Agent caller, ServiceType serviceType) {
		ServiceDescription service = new ServiceDescription();
		service.setType(serviceType.toString());
		DFAgentDescription template = new DFAgentDescription();
		template.addServices(service);
		AID[] foundAgents = null;
		try {
			DFAgentDescription[] result = DFService.search(caller, template);
			if (result == null) return null;
			if (result.length == 0) return new AID[0];
			foundAgents = new AID[result.length];
			for (int i = 0; i < result.length; i++)
				foundAgents[i] = result[i].getName();
		} catch (FIPAException e) {
			Logger logger = Logger.getJADELogger(caller.getLocalName());
			logger.warning(serviceType.getDescription() + " service search unsuccessful");
		}
		return foundAgents;
	}
}
