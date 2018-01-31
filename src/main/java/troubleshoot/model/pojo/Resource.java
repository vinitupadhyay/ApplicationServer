package troubleshoot.model.pojo;

import java.io.File;

public class Resource 
{
	private String name;
	private byte fileType;
	private byte fileId;
	private String logDirectoryPath;
	private String extension;
	
	public Resource(String name, byte fileType, byte fileId, String logDirectoryPath, String extension) 
	{
		this.name = name;
		this.fileId = fileId;
		this.fileType = fileType;
		this.logDirectoryPath = logDirectoryPath;
		this.extension = extension;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getFileType() {
		return fileType;
	}
	public void setFileType(byte fileType) {
		this.fileType = fileType;
	}
	public byte getFileId() {
		return fileId;
	}
	public void setFileId(byte fileId) {
		this.fileId = fileId;
	}
	
	public String getLogFilePath()
	{
		File logDirectory = new File(logDirectoryPath);
		if(!logDirectory.isDirectory())
		{
			logDirectory.mkdir();
		}
		return logDirectoryPath +"\\"+name+extension;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
