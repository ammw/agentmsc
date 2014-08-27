package eu.ammw.msc.plaga.common.task;

import eu.ammw.msc.plaga.common.Utils;
import jade.util.leap.Serializable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class configuration representation.
 *
 * @author AMW
 */
public class TaskConfiguration implements Serializable {
	private transient String directory;
	private transient boolean executedHere = false;

	private String[] arguments;
	private Map<String, Object> options;
	private Task task;
	private String id = "C" + (System.currentTimeMillis() % 1000000);

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public void setOption(String name, Object value) {
		if (options == null)
			options = new HashMap<String, Object>();

		if (options.containsKey(name))
			options.replace(name, value);
		else
			options.put(name, value);
	}

	void willExecute(Task task) {
		executedHere = true;
		setTask(task);
		initPath();
	}

	private void initPath() {
		String defaultDir = Utils.getProperty("exec.downloadDir");
		if (defaultDir == null)
			throw new NullPointerException("Directory is null!");
		this.directory = defaultDir + File.separator + task.getId() + File.separator;
		File f = new File(directory);
		this.directory = f.getAbsolutePath() + File.separator;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getDirectory() {
		if (executedHere && (directory == null))
			initPath();
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPath() {
		if (executedHere)
			return getDirectory() + task.getJar().getJarFileName();
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
