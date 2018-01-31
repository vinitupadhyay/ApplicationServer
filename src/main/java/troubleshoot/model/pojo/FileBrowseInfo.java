package troubleshoot.model.pojo;

public class FileBrowseInfo
{
	/**
	 * @brief Resource File Name field
	 */
	private String	fileName;	

	/**
	 * @brief File unique identification number
	 */
	private byte	fileId;	

	/**
	 * @brief File type id
	 */
	private byte	fileType;

	/**
	 * @brief File Authentication byte: 0 No Auth, 1 PIN Auth, 2 Non-PIN Auth
	 */
	private byte	fileAuthentication;

	/**
	 * @brief Resource crc on payload only
	 */
	private String	crc;
	
	
	public FileBrowseInfo()
	{
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte getFileId() {
		return fileId;
	}

	public void setFileId(byte fileId) {
		this.fileId = fileId;
	}

	public byte getFileType() {
		return fileType;
	}

	public void setFileType(byte fileType) {
		this.fileType = fileType;
	}

	public byte getFileAuthentication() {
		return fileAuthentication;
	}

	public void setFileAuthentication(byte fileAuthentication) {
		this.fileAuthentication = fileAuthentication;
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}
}
