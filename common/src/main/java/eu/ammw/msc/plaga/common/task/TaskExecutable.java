package eu.ammw.msc.plaga.common.task;

import jade.util.leap.Serializable;
import org.apache.commons.codec.binary.Base64;

/**
 * A single task's executable data.
 *
 * @author AMW
 */
public class TaskExecutable implements Serializable {
	protected static final String DEFAULT_JAR_FILE_NAME = "main.jar";

	private String id;
	private String jarFileName = DEFAULT_JAR_FILE_NAME;
	private byte[] fileContent;
	private String mainClass;

	public TaskExecutable(String id, byte[] file, String mainClassName) {
		this.id = id;
		this.fileContent = file;
		this.mainClass = mainClassName;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(fileContent.length * 3 / 2);
		builder.append("JAR ");
		builder.append(this.id);
		builder.append("\nFILE ");
		builder.append(this.jarFileName == null ? "" : this.jarFileName);
		builder.append("\n");
		if (fileContent != null)
			builder.append(new String(Base64.encodeBase64(this.fileContent)));
		return builder.toString();
	}
}
