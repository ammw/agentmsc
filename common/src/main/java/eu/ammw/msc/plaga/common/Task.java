package eu.ammw.msc.plaga.common;

import jade.util.leap.Serializable;
import org.apache.commons.codec.binary.Base64;

import java.io.File;

/**
 * A single task: its file, data and other stuff.
 *
 * @author AMW
 */
public class Task implements Serializable {
	protected static final String DEFAULT_JAR_FILE_NAME = "main.jar";

	private String id;
	private transient String directory;
	private String jarFileName = DEFAULT_JAR_FILE_NAME;
	private byte[] fileContent;
	private String[] args;
	private String mainClass;

	public Task(String id, byte[] file, String mainClassName, String[] args) {
		this.id = id;
		initPath();
		this.fileContent = file;
		this.args = args;
		this.mainClass = mainClassName;
	}

	@Deprecated
	public Task(String s) {
		// no args and classname!
		if (s.matches("TASK .+\\nFILE .*\\n.*")) {
			int pos1 = s.indexOf('\n');
			int pos2 = s.indexOf('\n', pos1 + 1);
			this.id = s.substring(5, pos1);
			this.jarFileName = s.substring(pos1 + 6, pos2);
			if (jarFileName.isEmpty()) jarFileName = DEFAULT_JAR_FILE_NAME;
			if (pos2 + 1 < s.length())
				this.fileContent = Base64.decodeBase64(s.substring(pos2 + 1).getBytes());
			if (this.fileContent != null && this.fileContent.length < 6)
				// ...then it's no valid JAR
				this.fileContent = null;
			assert (s.equals(this.toString()));
			initPath();
		} else {
			throw new IllegalArgumentException("Invalid Task form: " + s);
		}
	}

	public void initPath() {
		this.directory = Utils.getProperty("exec.downloadDir") + File.separator + id + File.separator;
		File f = new File(directory);
		this.directory = f.getAbsolutePath() + File.separator;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getStringFileContent() {
		return new String(Base64.encodeBase64(this.fileContent));
	}

	public byte[] getFileContent() {
		return this.fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = Base64.decodeBase64(fileContent.getBytes());
	}

	public String getMainClassName() {
		return mainClass;
	}

	public void setMainClassName(String mainClassName) {
		mainClass = mainClassName;
	}

	public String[] getArguments() {
		return args;
	}

	public void setArguments(String[] args) {
		this.args = args;
	}

	public String getJarFileName() {
		if (jarFileName == null)
			jarFileName = DEFAULT_JAR_FILE_NAME;
		return jarFileName;
	}

	public void setJarFileName(String jarFileName) {
		this.jarFileName = jarFileName;
	}

	public String getId() {
		return id;
	}

	public String getDirectory() {
		if (directory == null)
			initPath();
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPath() {
		return getDirectory() + getJarFileName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(fileContent.length * 3 / 2);
		builder.append("TASK ");
		builder.append(this.id);
		builder.append("\nFILE ");
		builder.append(this.jarFileName == null ? "" : this.jarFileName);
		builder.append("\n");
		if (fileContent != null)
			builder.append(new String(Base64.encodeBase64(this.fileContent)));
		return builder.toString();
	}
}
