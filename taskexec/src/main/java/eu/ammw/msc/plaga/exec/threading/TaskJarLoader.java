package eu.ammw.msc.plaga.exec.threading;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author AMW
 */
public class TaskJarLoader extends URLClassLoader {
	public TaskJarLoader(String fileName) throws MalformedURLException {
		super(new URL[]{new URL("file:///" + fileName)});
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		//if (true/* white-listed class - TODO change */)
		return super.loadClass(name);
		//return findClass(name); // TODO uncomment when having whitelist of classes
	}
}
