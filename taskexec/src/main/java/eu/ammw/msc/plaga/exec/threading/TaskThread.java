package eu.ammw.msc.plaga.exec.threading;

import eu.ammw.msc.plaga.common.Task;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;

/**
 * @author AMW
 */
public class TaskThread extends Thread {
	//private Object pass = new Object();
	private TaskJarLoader loader;
	//private TaskSecurityManager sm = new TaskSecurityManager(pass);
	private PrintStream std = System.out;
	private Task task;

	public TaskThread(Task task) throws MalformedURLException {
		super();
		this.loader = new TaskJarLoader(task.getPath());
		this.task = task;
	}

	@Override
	public void run() {
		// TODO uncomment this back for security
		//SecurityManager old = System.getSecurityManager();
		PrintStream ps = null;
		try {
			//new File(Resources.TMP_FILE_DIR).mkdir();
			File stdout = new File(task.getDirectory() + task.getMainClassName() + ".txt");
			ps = new PrintStream(stdout);
			System.setOut(ps);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.setSecurityManager(sm);
		runUntrustedCode();
		//sm.disable(pass);
		//System.setSecurityManager(old);
		if (ps != null)
			ps.close();
	}

	private void runUntrustedCode() {
		try {
			// run the main method
			loader.loadClass(task.getMainClassName()).getMethod("main", String[].class).invoke(null, (Object) task.getArguments());
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
