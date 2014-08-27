package eu.ammw.msc.plaga.exec.threading;

import eu.ammw.msc.plaga.common.task.Task;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;

/**
 * @author AMW
 */
public class TaskThread extends Thread {
	private TaskJarLoader loader;
	private PrintStream std = System.out;
	private Task task;

	public TaskThread(Task task) throws MalformedURLException {
		super();
		task.willExecute();
		this.loader = new TaskJarLoader(task.getConfig().getPath());
		this.task = task;
	}

	@Override
	public void run() {
		// TODO security
		PrintStream ps = null;
		try {
			File stdout = new File(task.getConfig().getDirectory() + task.getJar().getMainClassName() + ".txt");
			ps = new PrintStream(stdout);
			System.setOut(ps);
		} catch (IOException e) {
			e.printStackTrace();
		}

		runUntrustedCode();

		if (ps != null)
			ps.close();
	}

	private void runUntrustedCode() {
		try {
			// run the main method
			loader.loadClass(task.getJar().getMainClassName()).getMethod("main", String[].class).invoke(null, (Object) task.getConfig().getArguments());
			System.setOut(std);
			// TODO remove files
		} catch (Throwable t) {
			// FIXME
			t.printStackTrace();
		} finally {
			loader = null;
		}
	}

	public Object getStatus() {
		// TODO
		throw new Error("TaskThread.getStatus() not implemented!");
	}
}
