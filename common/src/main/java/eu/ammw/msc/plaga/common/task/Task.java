package eu.ammw.msc.plaga.common.task;

import jade.util.leap.Serializable;

/**
 * @author AMW
 */
public class Task implements Serializable {
	private TaskExecutable jar;
	private TaskConfiguration config;
	private String id;

	public Task(TaskExecutable executable) {
		if (executable == null)
			throw new NullPointerException("Executable cannot be null!");
		this.jar = executable;
		this.config = new TaskConfiguration();
		this.id = jar.getId() + "_" + config.getId() + "_" + (System.currentTimeMillis() % 1000000);
	}

	public TaskConfiguration getConfig() {
		return config;
	}

	public void setConfig(TaskConfiguration config) {
		this.config = config;
	}

	public TaskExecutable getJar() {
		return jar;
	}

	public void setJar(TaskExecutable jar) {
		this.jar = jar;
	}

	public void willExecute() {
		config.willExecute(this);
	}

	public String getId() {
		return id;
	}
}
