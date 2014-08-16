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
	private String id;
	private transient String directory;
	private String jarFileName = "main.jar";
	private byte[] fileContent;

	public Task(String id, byte[] file) {
		this.id = id;
		initPath();
		this.fileContent = file;
	}

	public Task(String s) {
		if (s.matches("TASK .+\\nFILE .*\\n.*")) {
			int pos1 = s.indexOf('\n');
			int pos2 = s.indexOf('\n', pos1 + 1);
			this.id = s.substring(5, pos1);
			this.jarFileName = s.substring(pos1 + 6, pos2);
			if (jarFileName.isEmpty()) jarFileName = "main.jar";
			if (pos2 + 1 < s.length())
				this.fileContent = Base64.decodeBase64(s.substring(pos2 + 1).getBytes());
			assert (s.equals(this.toString()));
			initPath();
		} else {
			throw new IllegalArgumentException("Invalid Task form: " + s);
		}
	}

	public void initPath() {
		this.directory = Utils.getProperty("exec.downloadDir") + File.separator + id + File.separator;
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

	public String getJarFileName() {
		return jarFileName;
	}

	public void setJarFileName(String jarFileName) {
		this.jarFileName = jarFileName;
	}

	public String getId() {
		return id;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getPath() {
		return directory + jarFileName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(fileContent.length * 3 / 2);
		builder.append("TASK ");
		builder.append(this.id);
		builder.append("\nFILE ");
		builder.append(this.jarFileName == null ? "" : this.jarFileName);
		builder.append("\n");
		builder.append(new String(Base64.encodeBase64(this.fileContent)));
		return builder.toString();
	}
}
